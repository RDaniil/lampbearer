package com.vdn.lampbearer.action.reactions;

import com.vdn.lampbearer.action.actions.DropItemAction;
import com.vdn.lampbearer.action.actions.PickUpItemAction;
import com.vdn.lampbearer.attributes.InventoryAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class DropItemReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        if (target.findAction(DropItemAction.class).isEmpty()) return false;
        if (!(target instanceof AbstractItem)) return false;

        Optional<InventoryAttr> inventoryOpt = initiator.findAttribute(InventoryAttr.class);
        if (inventoryOpt.isEmpty()) return false;

        inventoryOpt.get().removeItem((AbstractItem) target);
        context.getWorld().addEntity(target, initiator.getPosition());
        target.removeAction(DropItemAction.class);
        target.getActions().add(PickUpItemAction.getInstance());

        log.info(String.format("%s's been dropped", target.getName()));

        return true;
    }
}
