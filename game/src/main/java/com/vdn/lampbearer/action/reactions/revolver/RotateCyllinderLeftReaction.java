package com.vdn.lampbearer.action.reactions.revolver;

import com.vdn.lampbearer.action.reactions.Reaction;
import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.entites.item.firearm.Revolver;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class RotateCyllinderLeftReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        Optional<InventoryAttr> inventoryOpt = initiator.findAttribute(InventoryAttr.class);
        if (inventoryOpt.isEmpty()) return false;

        Optional<AbstractItem> foundFirearm = inventoryOpt.get().findItem(Revolver.class);
        if (foundFirearm.isEmpty()) {
            return false;
        }

        if (!(foundFirearm.get() instanceof Revolver)) {
            return false;
        }

        Revolver revolver = (Revolver) foundFirearm.get();
        revolver.rotateCylinderCounterClockwise();
        new CheckChamberReaction().execute(initiator, target, context);

        return true;
    }

}
