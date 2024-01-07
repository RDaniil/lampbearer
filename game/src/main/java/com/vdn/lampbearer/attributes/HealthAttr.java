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


    public int increaseHealth(int amount) {
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
        return health;
    }
}
