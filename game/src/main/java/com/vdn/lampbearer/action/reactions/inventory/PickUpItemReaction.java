package com.vdn.lampbearer.action.reactions.inventory;

import com.vdn.lampbearer.action.actions.inventory.DropItemAction;
import com.vdn.lampbearer.action.actions.inventory.PickUpItemAction;
import com.vdn.lampbearer.action.reactions.Reaction;
import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class PickUpItemReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        if (target.findAction(PickUpItemAction.class).isEmpty()) return false;
        if (!(target instanceof AbstractItem)) return false;

        Optional<InventoryAttr> inventoryOpt = initiator.findAttribute(InventoryAttr.class);
        if (inventoryOpt.isEmpty()) return false;

        inventoryOpt.get().putItem((AbstractItem) target);
        context.getWorld().removeEntity(target, initiator.getPosition());
        target.removeAction(PickUpItemAction.class);
        target.getActions().add(DropItemAction.getInstance());

        context.getLogArea()
                .addParagraph(String.format("%s's been picked up", target.getName()), false, 0);

        return true;
    }
}
