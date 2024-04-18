package com.vdn.lampbearer.attributes.creature;

import com.vdn.lampbearer.attributes.Attribute;
import com.vdn.lampbearer.entites.interfaces.Printable;
import lombok.Getter;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Component;
import org.hexworks.zircon.api.component.HBox;

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
        HBox attrBox = Components.hbox().withPreferredSize(15, 1).build();
        attrBox.addComponent(Components.label().withText("HP: ")
                .build());

        attrBox.addComponent(Components.label().withText(String.format("%s/%s", health, maxHealth)).build());

        return attrBox;
    }
}


