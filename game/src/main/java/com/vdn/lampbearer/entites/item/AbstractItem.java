package com.vdn.lampbearer.entites.item;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.interfaces.Printable;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Component;
import org.hexworks.zircon.api.component.HBox;

import static org.hexworks.zircon.api.ComponentDecorations.noDecoration;

public abstract class AbstractItem extends AbstractEntity implements Printable {

    @Override
    public Component toComponent() {
        HBox inventoryRow = Components.hbox()
                .withPreferredSize(getName().length() + 2, 1)
                .build();

        var itemIcon = Components.icon()
                .withIcon(getTile())
                .build();

        var itemComponent = Components.label()
                .withDecorations(noDecoration())
                .withText(" " + getName())
                .build();

        inventoryRow.addComponent(itemIcon);
        inventoryRow.addComponent(itemComponent);
        return inventoryRow;
    }
}
