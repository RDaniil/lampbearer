package com.vdn.lampbearer.action.reactions.inventory;

import com.vdn.lampbearer.action.actions.inventory.DropLightSourceAction;
import com.vdn.lampbearer.action.actions.inventory.PickUpLightSourceAction;
import com.vdn.lampbearer.action.reactions.Reaction;
import com.vdn.lampbearer.attributes.LightSourceAttr;
import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.services.light.Light;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class PickUpLightSourceReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity lightSource, GameContext context) {
        if (lightSource.findAction(PickUpLightSourceAction.class).isEmpty()) return false;
        if (!(lightSource instanceof AbstractItem)) return false;
        Optional<LightSourceAttr> lightSourceAttr = lightSource.findAttribute(LightSourceAttr.class);
        if (lightSourceAttr.isEmpty()) return false;

        Optional<InventoryAttr> inventoryOpt = initiator.findAttribute(InventoryAttr.class);
        if (inventoryOpt.isEmpty()) return false;

        inventoryOpt.get().putItem((AbstractItem) lightSource);
        context.getWorld().removeEntity(lightSource, initiator.getPosition());
        if (lightSourceAttr.get().isOn()) {
            Light light = lightSourceAttr.get().getLight();
            context.getWorld().removeStaticLight(light);
            context.getWorld().addDynamicLight(initiator, light);
        }

        lightSource.removeAction(PickUpLightSourceAction.class);
        lightSource.getActions().add(DropLightSourceAction.getInstance());

        log.info(String.format("%s's been picked up", lightSource.getName()));

        return true;
    }
}
