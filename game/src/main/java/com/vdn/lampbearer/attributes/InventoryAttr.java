package com.vdn.lampbearer.attributes;

import com.vdn.lampbearer.entites.item.AbstractItem;
import lombok.Getter;

@Getter
public class InventoryAttr extends AbstractInventoryAttr<AbstractItem> {

    public InventoryAttr(int maxSize) {
        super(maxSize, AbstractItem.class);
    }

}
