package com.vdn.lampbearer.action.reactions.items;

import com.vdn.lampbearer.action.reactions.Reaction;
import com.vdn.lampbearer.attributes.RoundContainerAttr;
import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.entites.item.firearm.AbstractFirearm;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class LoadFirearmReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity ammoBox, GameContext context) {
        Optional<InventoryAttr> inventoryOpt = initiator.findAttribute(InventoryAttr.class);
        if (inventoryOpt.isEmpty()) return false;

        Optional<AbstractItem> foundFirearm = inventoryOpt.get().findItem(AbstractFirearm.class);
        if (foundFirearm.isEmpty()) {
            return false;
        }
        //TODO: Рак, все желтое, надо понять как красиво дженерично провернуть контейнеры
        Optional<RoundContainerAttr> ammoBoxInventory =
                ammoBox.findAttribute(RoundContainerAttr.class);
        if (ammoBoxInventory.isEmpty()) {
            return false;
        }

        if (foundFirearm.get() instanceof AbstractFirearm) {
            var firearm = (AbstractFirearm<?>) foundFirearm.get();
            if (!ammoBoxInventory.get().isItemTypeSuitable(firearm.getAllowedProjectileType())) {
                return false;
            }

            int roundsLoaded = firearm.loadAllRounds(ammoBoxInventory.get());
            context.getLogArea().addParagraph(String.format("%s rounds loaded, %s left in a box.",
                    roundsLoaded, ammoBoxInventory.get().getItemCount()), false, 0);

            deleteAmmoBoxIfEmpty((AbstractItem) ammoBox, ammoBoxInventory.get(), inventoryOpt.get());
        } else {
            return false;
        }

        return true;
    }


    private static void deleteAmmoBoxIfEmpty(AbstractItem ammoBox, RoundContainerAttr ammoBoxInventory,
                                             InventoryAttr inventoryOpt) {
        if (ammoBoxInventory.getItemCount() == 0) {
            inventoryOpt.removeItem(ammoBox);
        }
    }
}
