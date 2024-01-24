package com.vdn.lampbearer.action.reactions;

import com.vdn.lampbearer.action.actions.DropLightSourceAction;
import com.vdn.lampbearer.action.actions.PickLightSourceAction;
import com.vdn.lampbearer.action.actions.PickUpItemAction;
import com.vdn.lampbearer.attributes.InventoryAttr;
import com.vdn.lampbearer.attributes.LightSourceAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class PickUpLightSourceReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        if (target.findAction(PickUpItemAction.class).isEmpty()) return false;
        if (!(target instanceof AbstractItem)) return false;
        Optional<LightSourceAttr> lightSource = target.findAttribute(LightSourceAttr.class);
        if (lightSource.isEmpty()) return false;

        Optional<InventoryAttr> inventoryOpt = initiator.findAttribute(InventoryAttr.class);
        if (inventoryOpt.isEmpty()) return false;

        inventoryOpt.get().putItem((AbstractItem) target);
        context.getWorld().removeEntity(target, initiator.getPosition());
        context.getWorld().removeStaticLight(lightSource.get().getLight());

        target.removeAction(PickLightSourceAction.class);
        target.getActions().add(DropLightSourceAction.getInstance());

        log.info(String.format("%s's been picked up", target.getName()));

        return true;
    }
}
