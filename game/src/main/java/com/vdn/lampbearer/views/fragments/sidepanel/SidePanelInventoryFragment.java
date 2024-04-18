package com.vdn.lampbearer.views.fragments.sidepanel;

import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.entites.item.AbstractItem;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.AttachedComponent;
import org.hexworks.zircon.api.component.Component;
import org.hexworks.zircon.api.component.Fragment;
import org.hexworks.zircon.api.component.VBox;
import org.hexworks.zircon.api.graphics.BoxType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.vdn.lampbearer.config.GameConfig.SIDEBAR_WIDTH;
import static org.hexworks.zircon.api.ComponentDecorations.box;

/**
 * UI-компонент инветаря
 */
@Slf4j
public class SidePanelInventoryFragment implements Fragment {

    private final VBox inventoryRow;
    private final Map<AbstractItem, AttachedComponent> itemToFragmentMap = new HashMap<>();
    private final InventoryAttr inventory;


    public SidePanelInventoryFragment(InventoryAttr inventory) {
        this.inventory = inventory;
        inventoryRow = Components.vbox()
                .withPreferredSize(SIDEBAR_WIDTH - 2, inventory.getMaxSize())
                .withDecorations(box(BoxType.SINGLE, "Inventory"))
                .withUpdateOnAttach(true)
                .build();
    }


    @NotNull
    @Override
    public Component getRoot() {
        addInventoryItems();
        return inventoryRow;
    }


    public void updateContent() {
        var itemsToRemove = new HashSet<AbstractItem>();

        itemToFragmentMap.forEach((item, attachedComponent) -> {
            if (!inventory.getItems().contains(item)) {
                attachedComponent.detach();
                itemsToRemove.add(item);
            }
        });

        itemToFragmentMap.keySet().removeAll(itemsToRemove);
        addInventoryItems();
    }


    private void addInventoryItems() {
        inventory.getItems().forEach(this::addInventoryItem);
    }


    private void addInventoryItem(AbstractItem item) {
        if (!itemToFragmentMap.containsKey(item)) {
            AttachedComponent attachedComponent = inventoryRow
                    .addFragment(new SidePanelInventoryRowFragment(item));
            itemToFragmentMap.put(item, attachedComponent);
        }
    }
}
