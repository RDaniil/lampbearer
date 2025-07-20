package com.vdn.lampbearer.attributes;

import com.vdn.lampbearer.entites.item.AbstractItem;
import lombok.Getter;

@Getter
public class ItemContainerAttr<T extends AbstractItem> extends AbstractInventoryAttr<T> {

    public ItemContainerAttr(int maxSize, Class<? extends T> itemType) {
        super(maxSize, itemType);
    }


//    public static <T extends AbstractItem> ItemContainerAttr<T> createFilled(
//            int maxSize, T itemToFillWith, Class<T> itemType) {
//        var instance = new ItemContainerAttr<T>(maxSize, itemType);
//        for (int i = 0; i < maxSize; i++) {
//            instance.putItem(itemToFillWith);
//        }
//        return instance;
//    }
}
