package com.vdn.lampbearer.attributes;

import com.vdn.lampbearer.action.actions.Action;
import com.vdn.lampbearer.entites.item.AbstractItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public abstract class AbstractInventoryAttr<T extends AbstractItem> implements Attribute {

    private final int maxSize;

    private final List<T> items = new ArrayList<>();

    private final Class<? extends T> itemType;


    public boolean isItemTypeSuitable(Class<?> itemType) {
        return itemType.isAssignableFrom(this.itemType);
    }


    public int getItemCount() {
        return items.size();
    }


    public Optional<T> popItem() {
        if (items.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(items.remove(0));
    }


    public Optional<? extends T> popItem(Class<? extends T> itemType) {
        Optional<? extends T> item = findItem(itemType);
        item.ifPresent(abstractItem -> items.remove(abstractItem));

        return item;
    }


    public Optional<T> findItem(Class<? extends T> itemType) {
        return items.stream()
                .filter(itemType::isInstance)
                .findFirst();
    }


    public <A extends Action<?>> List<T> findItemByAction(Class<A> itemAction) {
        return items.stream()
                .filter(item -> item.findAction(itemAction).isPresent())
                .collect(Collectors.toList());
    }


    public <A extends Attribute> List<T> findByAttribute(Class<A> itemAttribute) {
        return items.stream()
                .filter(item -> item.findAttribute(itemAttribute).isPresent())
                .collect(Collectors.toList());
    }


    public boolean putItem(T item) {
        if (items.size() == maxSize) {
            return false;
        }

        return items.add(item);
    }


    public boolean removeItem(T item) {
        return items.remove(item);
    }
}
