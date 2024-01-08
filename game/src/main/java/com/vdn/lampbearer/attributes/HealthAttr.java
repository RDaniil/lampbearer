package com.vdn.lampbearer.attributes;

import com.vdn.lampbearer.entites.Printable;
import lombok.Getter;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Component;

@Getter
public class HealthAttr implements Attribute, Printable {
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


    @Override
    public Component toComponent() {
        return Components.label()
                .withText(String.format("HP: %s/%s", health, maxHealth))
                .build();
    }
}
