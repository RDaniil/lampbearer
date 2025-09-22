package com.vdn.lampbearer.attributes.creature;

import com.vdn.lampbearer.attributes.Attribute;
import com.vdn.lampbearer.services.DiceBuilder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StrengthAttr implements Attribute {
    private DiceBuilder value;

    public int getValue(){
        return value.roll();
    }
}
