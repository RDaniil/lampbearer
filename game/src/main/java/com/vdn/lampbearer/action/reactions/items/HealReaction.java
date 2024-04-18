package com.vdn.lampbearer.action.reactions.items;

import com.vdn.lampbearer.action.reactions.Reaction;
import com.vdn.lampbearer.attributes.creature.HealthAttr;
import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.attributes.items.HealingItemAttribute;
import com.vdn.lampbearer.attributes.items.UsableAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Actor;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class HealReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity target, AbstractEntity healingItem, GameContext context) {
        if (target.findAttribute(HealthAttr.class).isEmpty()) return false;
        if (!(target instanceof Actor)) return false;

        Optional<HealingItemAttribute> healingItemAttribute =
                healingItem.findAttribute(HealingItemAttribute.class);
        if (healingItemAttribute.isEmpty()) return false;

        HealthAttr healthAttr = target.findAttribute(HealthAttr.class).get();
        healthAttr.increaseHealth(healingItemAttribute.get().getHealingAmount());

        Optional<UsableAttr> usableAttribute =
                healingItem.findAttribute(UsableAttr.class);
        if (usableAttribute.isEmpty()) {
            throw new RuntimeException("Healing item without usable " +
                    "attribute");
        }

        Optional<InventoryAttr> inventoryOpt = target.findAttribute(InventoryAttr.class);
        if (inventoryOpt.isEmpty()) return false;

        if (usableAttribute.get().useOnce() == 0) {
            //TODO: 1) Если будет возможность хилять не только себя - это не сработает, т.к. надо
            // удалять из чужого инвентаря а не из нашего. 2) Наверное не очень хорошо, что
            // действие лечения заботится о том чтобы удалить используемый предмет...
            inventoryOpt.get().removeItem((AbstractItem) healingItem);
        }

        log.info(String.format("%s's been healed by %s", target.getName(), healingItem.getName()));

        return true;
    }
}
