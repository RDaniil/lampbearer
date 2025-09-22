package com.vdn.lampbearer.action.reactions.revolver;

import com.vdn.lampbearer.action.reactions.Reaction;
import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.entites.item.firearm.AbstractFirearm;
import com.vdn.lampbearer.entites.item.projectile.Round;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class LoadOneInFirearmReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity round, GameContext context) {
        Optional<InventoryAttr> inventoryOpt = initiator.findAttribute(InventoryAttr.class);
        if (inventoryOpt.isEmpty()) return false;

        Optional<AbstractItem> foundFirearm = inventoryOpt.get().findItem(AbstractFirearm.class);
        if (foundFirearm.isEmpty()) {
            context.getLogArea().addParagraph("No firearm to load this ammo", false, 0);
            return false;
        }

        if (!(foundFirearm.get() instanceof AbstractFirearm)) {
            return false;
        }

        var firearm = (AbstractFirearm<?>) foundFirearm.get();
        if (!firearm.getAllowedProjectileType().isAssignableFrom(round.getClass())) {
            context.getLogArea().addParagraph(String.format("Cannot load %s into %s",
                    round.getName(), firearm.getName()), false, 0);
            return false;
        }

        if (firearm.countEmptyRounds() == 0) {
            context.getLogArea().addParagraph(String.format("Cannot load, %s is full",
                    firearm.getName()), false, 0);
            return false;
        }

        int roundsLoaded = firearm.loadOneRound((Round) round);
        if (roundsLoaded > 0) {
            inventoryOpt.get().removeItem((AbstractItem) round);
            context.getLogArea().addParagraph(String.format("%s rounds loaded", roundsLoaded),
                    false, 0);
            new CheckChamberReaction().execute(initiator, round, context);
        }
        return true;
    }
}
