package com.vdn.lampbearer.attributes;

import com.vdn.lampbearer.entites.item.projectile.Round;

import java.util.function.Supplier;

public class RoundContainerAttr extends ItemContainerAttr<Round> {
    public RoundContainerAttr(int maxSize, Class<? extends Round> itemType) {
        super(maxSize, itemType);
    }

    public static RoundContainerAttr createFilled(int maxSize, Supplier<? extends Round> roundSupplier,
                                                  Class<? extends Round> itemType) {
        var instance = new RoundContainerAttr(maxSize, itemType);
        for (int i = 0; i < maxSize; i++) {
            instance.putItem(roundSupplier.get());
        }
        return instance;
    }
}
