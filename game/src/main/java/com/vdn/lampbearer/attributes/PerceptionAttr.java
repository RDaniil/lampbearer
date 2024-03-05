package com.vdn.lampbearer.attributes;

import lombok.Getter;

@Getter
public class PerceptionAttr implements Attribute {

    private final int value;


    public PerceptionAttr(int value) {
        if (value < 1) throw new RuntimeException("Perception value is less that 1");
        this.value = value;
    }
}
