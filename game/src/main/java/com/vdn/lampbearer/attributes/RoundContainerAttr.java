package com.vdn.lampbearer.attributes;

import com.vdn.lampbearer.entites.item.projectile.Round;

public class RoundContainerAttr extends ItemContainerAttr<Round> {
    public RoundContainerAttr(int maxSize, Class<Round> itemType) {
        super(maxSize, itemType);
    }
}
