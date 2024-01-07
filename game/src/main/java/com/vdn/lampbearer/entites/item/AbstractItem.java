package com.vdn.lampbearer.entites.item;

import com.vdn.lampbearer.action.interaction.DropItemAction;
import com.vdn.lampbearer.action.interaction.PickUpItemAction;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Printable;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Component;
import org.hexworks.zircon.api.component.HBox;

import static org.hexworks.zircon.api.ComponentDecorations.noDecoration;

public abstract class AbstractItem extends AbstractEntity implements Printable {

    public AbstractItem() {
        getActions().add(DropItemAction.getInstance());
        getActions().add(PickUpItemAction.getInstance());
    }


    @Override
    public Component toComponent() {
        HBox inventoryRow = Components.hbox()
                .withPreferredSize(getName().length() + 2, 1)
                .build();

        var itemIcon = Components.icon()
                .withIcon(getTile())
                .build();

        var itemComponent = Components.button()
                .withDecorations(noDecoration())
                .withText(" " + getName())
                .build();

        inventoryRow.addComponent(itemIcon);
        inventoryRow.addComponent(itemComponent);
        return inventoryRow;
    }
}
