package com.vdn.lampbearer.views.fragments;

import com.vdn.lampbearer.entites.item.AbstractItem;
import lombok.RequiredArgsConstructor;
import org.hexworks.zircon.api.component.Component;
import org.hexworks.zircon.api.component.Fragment;
import org.jetbrains.annotations.NotNull;

/**
 * UI-компонент строки инвенторя (отображение одного предмета
 */
@RequiredArgsConstructor
public class InventoryRowFragment implements Fragment {

    private final AbstractItem item;


    @NotNull
    @Override
    public Component getRoot() {
        return item.toComponent();
    }
}
