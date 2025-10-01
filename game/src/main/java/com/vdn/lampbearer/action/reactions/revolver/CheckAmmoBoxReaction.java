package com.vdn.lampbearer.action.reactions.revolver;

import com.vdn.lampbearer.action.reactions.Reaction;
import com.vdn.lampbearer.attributes.RoundContainerAttr;
import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class CheckAmmoBoxReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity ammoBox, GameContext context) {
        Optional<InventoryAttr> inventoryOpt = initiator.findAttribute(InventoryAttr.class);
        if (inventoryOpt.isEmpty()) return false;

        Optional<RoundContainerAttr> ammoBoxInventory =
                ammoBox.findAttribute(RoundContainerAttr.class);

        context.getLogArea().addParagraph(
                String.format("%s rounds left in a box.", ammoBoxInventory.get().getItemCount()));

        return false;
    }

}
