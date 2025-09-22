package com.vdn.lampbearer.attributes.items;

import com.vdn.lampbearer.attributes.Attribute;
import com.vdn.lampbearer.services.DiceBuilder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HealingItemAttribute implements Attribute {
    private final DiceBuilder healingAmount;

    public int getHealingAmount() {
        return healingAmount.roll();
    }
}
