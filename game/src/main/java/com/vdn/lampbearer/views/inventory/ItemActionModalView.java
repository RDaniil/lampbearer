package com.vdn.lampbearer.views.inventory;

import com.vdn.lampbearer.config.GameConfig;
import com.vdn.lampbearer.dto.ItemUseReactionContextDto;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.views.fragments.inventory.ItemUseFragment;
import kotlin.Unit;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.builder.component.ModalBuilder;
import org.hexworks.zircon.api.component.Button;
import org.hexworks.zircon.api.component.ComponentAlignment;
import org.hexworks.zircon.api.fragment.menu.MenuItemSelected;
import org.hexworks.zircon.api.fragment.menu.SelectionCancelled;
import org.hexworks.zircon.api.graphics.BoxType;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.hexworks.zircon.api.ComponentDecorations.box;
import static org.hexworks.zircon.api.ComponentDecorations.noDecoration;

public class ItemActionModalView {
    @Nullable
    public static ItemUseReactionContextDto showItemActionModal(GameContext context, AbstractItem item) {
        var panel = Components.panel()
                .withPreferredSize(27, item
                        .getActions().size() + 3)
                .withDecorations(box(BoxType.SINGLE, "Use item"))
                .build();

        var modal = ModalBuilder.newBuilder()
                .withPreferredSize(panel.getSize())
                .withComponent(panel)
                .withAlignmentWithin(context.getScreen(), ComponentAlignment.CENTER)
                .withTileset(GameConfig.getAppConfig().getDefaultTileset())
                .withColorTheme(GameConfig.THEME)
                .build();

        ItemUseFragment itemUseFragment = new ItemUseFragment(item, context, modal);

        panel.addFragment(itemUseFragment);

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

        AtomicReference<ItemUseReactionContextDto> selectedItemActionContext =
                new AtomicReference<>(null);
        modal.onClosed(modalResult -> {
            if (modalResult instanceof MenuItemSelected) {
                selectedItemActionContext.set(((MenuItemSelected<ItemUseReactionContextDto>) modalResult).getItem());
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

        return selectedItemActionContext.get();
    }
}
