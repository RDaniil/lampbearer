package com.vdn.lampbearer.attributes.items;

import com.vdn.lampbearer.attributes.Attribute;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HealingItemAttribute implements Attribute {
    private final int healingAmount;
}
