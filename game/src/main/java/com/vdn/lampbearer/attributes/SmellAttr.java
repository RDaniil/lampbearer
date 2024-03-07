package com.vdn.lampbearer.attributes;

import lombok.Getter;

/**
 * @author Chizhov D. on 2024.03.07
 */
@Getter
public class SmellAttr implements Attribute {

    private final int value;


    public SmellAttr(int value) {
        if (value < 1) throw new RuntimeException("Smell value is less that 1");
        this.value = value;
    }
}
