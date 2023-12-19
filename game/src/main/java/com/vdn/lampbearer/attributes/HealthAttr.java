package com.vdn.lampbearer.attributes;

import lombok.Getter;

@Getter
public class HealthAttr implements Attribute {
    private final int maxHealth;
    private int health;

    public HealthAttr(int maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public int reduceHealth(int reduceAmount) {
        health -= reduceAmount;
        return health;
    }
}
