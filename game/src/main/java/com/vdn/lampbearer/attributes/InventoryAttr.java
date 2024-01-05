package com.vdn.lampbearer.attributes;

import com.vdn.lampbearer.entites.item.AbstractItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class InventoryAttr implements Attribute {

    private int maxSize = 10;

    List<AbstractItem> items = new ArrayList<>();


    public Optional<AbstractItem> popItem(Class<AbstractItem> itemType) {
        Optional<AbstractItem> item = findItem(itemType);
        item.ifPresent(abstractItem -> items.remove(abstractItem));

        return item;
    }


    public Optional<AbstractItem> findItem(Class<AbstractItem> itemType) {
        return items.stream()
                .filter(itemType::isInstance)
                .findFirst();
    }


    public boolean putItem(AbstractItem item) {
        if (items.size() == maxSize) {
            return false;
        }

        return items.add(item);
    }
}
