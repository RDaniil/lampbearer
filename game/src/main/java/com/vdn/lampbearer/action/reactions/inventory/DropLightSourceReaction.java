package com.vdn.lampbearer.action.reactions.inventory;

import com.vdn.lampbearer.action.actions.inventory.DropLightSourceAction;
import com.vdn.lampbearer.action.actions.inventory.PickUpLightSourceAction;
import com.vdn.lampbearer.action.reactions.Reaction;
import com.vdn.lampbearer.attributes.LightSourceAttr;
import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class DropLightSourceReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity lightSource, GameContext context) {
        if (lightSource.findAction(DropLightSourceAction.class).isEmpty()) return false;
        if (!(lightSource instanceof AbstractItem)) return false;
        Optional<LightSourceAttr> lightAttr = lightSource.findAttribute(LightSourceAttr.class);
        if (lightAttr.isEmpty()) return false;

        Optional<InventoryAttr> inventoryOpt = initiator.findAttribute(InventoryAttr.class);
        if (inventoryOpt.isEmpty()) return false;

        inventoryOpt.get().removeItem((AbstractItem) lightSource);

        context.getWorld().addEntity(lightSource, initiator.getPosition());
        if (lightAttr.get().isOn()) {
            context.getWorld().removeDynamicLight(initiator, lightAttr.get().getLight());
            context.getWorld().addStaticLight(lightAttr.get().getLight());
        }

        lightSource.removeAction(DropLightSourceAction.class);
        lightSource.getActions().add(PickUpLightSourceAction.getInstance());

        log.info(String.format("%s's been dropped", lightSource.getName()));
        context.getLogArea()
                .addParagraph(String.format("%s's been dropped", lightSource.getName()), false, 0);


        return true;
    }
}
