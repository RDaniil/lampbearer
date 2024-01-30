package com.vdn.lampbearer.attributes;

import com.vdn.lampbearer.action.Action;
import com.vdn.lampbearer.entites.item.AbstractItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


    public <T extends Action<?>> List<AbstractItem> findItemByAction(Class<T> itemAction) {
        return items.stream()
                .filter(item -> item.findAction(itemAction).isPresent())
                .collect(Collectors.toList());
    }


    public <T extends Attribute> List<AbstractItem> findByAttribute(Class<T> itemAttribute) {
        return items.stream()
                .filter(item -> item.findAttribute(itemAttribute).isPresent())
                .collect(Collectors.toList());
    }


    public boolean putItem(AbstractItem item) {
        if (items.size() == maxSize) {
            return false;
        }

        return items.add(item);
    }


    public boolean removeItem(AbstractItem item) {
        return items.remove(item);
    }
}
