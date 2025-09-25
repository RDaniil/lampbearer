package com.vdn.lampbearer.action.reactions.items;

import com.vdn.lampbearer.action.actions.items.FuelLanternAction;
import com.vdn.lampbearer.action.reactions.Reaction;
import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.attributes.items.FueledByOilAttr;
import com.vdn.lampbearer.attributes.items.UsableAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.entites.item.Lantern;
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

        if (oilLamps.isEmpty()) {
            return false;
        }
        Lantern lampToFuel = (Lantern) oilLamps.get(0);

        for (AbstractItem oilLamp : oilLamps) {
            Lantern lamp = (Lantern) oilLamp;
            if (lamp.getFuelLeftNumber() < lampToFuel.getFuelLeftNumber()) {
                lampToFuel = lamp;
            }
            if(lamp.getLightSource().isOn()){
                lampToFuel = lamp;
            }
        }

        Optional<UsableAttr> oilLampUses = lampToFuel.findAttribute(UsableAttr.class);
        if (oilLampUses.isEmpty()) {
            return false;
        }

        int addedFuel = oilLampUses.get().addUses(oilBottleUses.get());
        if (oilBottleUses.get().getUsesLeft() <= 0) {
            inventoryOpt.get().removeItem((AbstractItem) oilBottle);
        }
        context.getLogArea()
                .addParagraph(String.format("%s's been fueled, it's %s now", lampToFuel.getName(),
                        lampToFuel.getFuelLeft()), false, 0);
        log.info(String.format("%s's been fuelled by %d oil", oilBottle.getName(), addedFuel));

        return true;
    }
}
