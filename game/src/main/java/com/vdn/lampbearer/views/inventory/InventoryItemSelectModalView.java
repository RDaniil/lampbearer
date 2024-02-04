package com.vdn.lampbearer.views.inventory;

import com.vdn.lampbearer.attributes.InventoryAttr;
import com.vdn.lampbearer.config.GameConfig;
import com.vdn.lampbearer.dto.ItemUseReactionContextDto;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.views.fragments.ModalInventoryFragment;
import kotlin.Unit;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.builder.component.ModalBuilder;
import org.hexworks.zircon.api.component.Button;
import org.hexworks.zircon.api.component.ComponentAlignment;
import org.hexworks.zircon.api.fragment.menu.MenuItemSelected;
import org.hexworks.zircon.api.fragment.menu.SelectionCancelled;
import org.hexworks.zircon.api.graphics.BoxType;
import org.hexworks.zircon.internal.component.modal.EmptyModalResult;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.hexworks.zircon.api.ComponentDecorations.box;
import static org.hexworks.zircon.api.ComponentDecorations.noDecoration;

public class InventoryItemSelectModalView {
    public static ItemUseReactionContextDto showItemActionModal(GameContext context, InventoryAttr inventoryAttr) {
        Integer maxItemNameLength = inventoryAttr.getItems().stream()
                .map(i -> i.getName().length())
                .max(Integer::compareTo).orElse(23);

        var panel = Components.panel()
                .withPreferredSize(maxItemNameLength + 12, inventoryAttr.getMaxSize() + 3)
                .withDecorations(box(BoxType.SINGLE, "Select item"))
                .build();

        var modal = ModalBuilder.newBuilder()
                .withPreferredSize(panel.getSize())
                .withComponent(panel)
                .withAlignmentWithin(context.getScreen(), ComponentAlignment.CENTER)
                .withTileset(GameConfig.getAppConfig().getDefaultTileset())
                .withColorTheme(GameConfig.THEME)
                .build();

        ModalInventoryFragment modalInventoryFragment = new ModalInventoryFragment(
                inventoryAttr,
                modal,
                panel.getWidth() - 3);

        panel.addFragment(modalInventoryFragment);

        Button close = Components.button()
                .withText("Close")
                .withDecorations(noDecoration())
                .withAlignmentWithin(panel, ComponentAlignment.BOTTOM_RIGHT)
                .build();

        close.onActivated(componentEvent -> {
            modal.close(SelectionCancelled.INSTANCE);
            return Unit.INSTANCE;
        });

        panel.addComponent(close);

        CountDownLatch countDownLatch = new CountDownLatch(1);

        AtomicReference<AbstractItem> selectedItem = new AtomicReference<>(null);
        modal.onClosed(modalResult -> {
            if (modalResult instanceof EmptyModalResult) {
                selectedItem.set(null);
            } else if (modalResult instanceof MenuItemSelected) {
                selectedItem.set(((MenuItemSelected<AbstractItem>) modalResult).getItem());
            }
            countDownLatch.countDown();
            return Unit.INSTANCE;
        });

        context.getScreen().openModal(modal);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (selectedItem.get() != null) {
            return ItemActionModalView.showItemActionModal(context, selectedItem.get());
        }

        return null;
    }
}
