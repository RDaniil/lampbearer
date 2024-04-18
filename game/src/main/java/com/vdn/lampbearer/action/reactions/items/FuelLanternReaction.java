package com.vdn.lampbearer.action.reactions.items;

import com.vdn.lampbearer.action.actions.items.FuelLanternAction;
import com.vdn.lampbearer.action.reactions.Reaction;
import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.attributes.items.FueledByOilAttr;
import com.vdn.lampbearer.attributes.items.UsableAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class FuelLanternReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity oilBottle,
                           GameContext context) {
        if (!(oilBottle instanceof AbstractItem)) return false;
        if (oilBottle.findAction(FuelLanternAction.class).isEmpty()) return false;

        Optional<UsableAttr> oilBottleUses = oilBottle.findAttribute(UsableAttr.class);
        if (oilBottleUses.isEmpty()) return false;

        Optional<InventoryAttr> inventoryOpt = initiator.findAttribute(InventoryAttr.class);
        if (inventoryOpt.isEmpty()) return false;
        List<AbstractItem> oilLamps = inventoryOpt.get()
                .findByAttribute(FueledByOilAttr.class);

        //TODO: Если в инвентаре две лампы - это не рабоатет
        if (oilLamps.isEmpty()) {
            return false;
        }
        AbstractItem oilLamp = oilLamps.get(0);

        Optional<UsableAttr> oilLampUses = oilLamp.findAttribute(UsableAttr.class);
        if (oilLampUses.isEmpty()) {
            return false;
        }

        int addedFuel = oilLampUses.get().addUses(oilBottleUses.get());
        context.getLogArea()
                .addParagraph(String.format("%s's been fueled, it's %s now", oilLamp.getName(),
                        oilLamp.getDescription()), false, 0);
        log.info(String.format("%s's been fuelled by %d oil", oilBottle.getName(), addedFuel));

        return true;
    }
}
