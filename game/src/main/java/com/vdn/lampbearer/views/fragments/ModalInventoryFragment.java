package com.vdn.lampbearer.views.fragments;

import com.vdn.lampbearer.attributes.InventoryAttr;
import com.vdn.lampbearer.config.GameConfig;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.views.fragments.sidepanel.SidePanelInventoryRowFragment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Component;
import org.hexworks.zircon.api.component.Fragment;
import org.hexworks.zircon.api.component.HBox;
import org.hexworks.zircon.api.component.VBox;
import org.hexworks.zircon.api.component.modal.Modal;
import org.hexworks.zircon.api.component.modal.ModalResult;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.fragment.menu.MenuItemSelected;
import org.hexworks.zircon.api.uievent.*;
import org.hexworks.zircon.internal.component.modal.EmptyModalResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.hexworks.zircon.api.ComponentDecorations.noDecoration;

/**
 * UI-компонент инветаря
 */
@Slf4j
@RequiredArgsConstructor
public class ModalInventoryFragment implements Fragment {

    private static final String itemLabels = "abcdefghijklmnopqrstuvwxyz";

    private final InventoryAttr inventory;
    private final Modal<ModalResult> containingModal;
    private final int width;


    @NotNull
    @Override
    public Component getRoot() {
        VBox inventoryRow = Components.vbox()
                .withPreferredSize(width, inventory.getMaxSize())
                .withDecorations(noDecoration())
                .build();

        List<AbstractItem> items = inventory.getItems();
        for (int i = 0; i < items.size(); i++) {

            AbstractItem item = items.get(i);
            HBox itemOption = Components.hbox()
                    .withPreferredSize(width, 1)
                    .withDecorations(noDecoration())
                    .build();

            itemOption.addComponent(Components.icon()
                    .withIcon(Tile.newBuilder().withCharacter(itemLabels.charAt(i))
                            .withBackgroundColor(GameConfig.THEME.getAccentColor())
                            .withForegroundColor(GameConfig.THEME.getSecondaryForegroundColor())
                            .build()));

            itemOption.addComponent(Components.icon()
                    .withIcon(Tile.newBuilder().withCharacter(':')
                            .withBackgroundColor(GameConfig.THEME.getAccentColor())
                            .withForegroundColor(GameConfig.THEME.getSecondaryForegroundColor())
                            .build()));

            itemOption.addFragment(new SidePanelInventoryRowFragment(item))
                    .handleMouseEvents(MouseEventType.MOUSE_PRESSED, (mouseEvent, uiEventPhase) -> {
                        if (uiEventPhase.equals(UIEventPhase.CAPTURE)) {
                            containingModal.close(new MenuItemSelected<>(item));
                        }
                        return UIEventResponse.processed();
                    });

            inventoryRow.addComponent(itemOption);

            int finalI = i;
            containingModal.handleKeyboardEvents(
                    KeyboardEventType.KEY_PRESSED,
                    (event, uiEventPhase) -> {
                        if (uiEventPhase.equals(UIEventPhase.TARGET)) {
                            if (event.getKey().toLowerCase().charAt(0) == itemLabels.charAt(finalI)) {
                                containingModal.close(new MenuItemSelected<>(item));
                            } else if (event.getCode().equals(KeyCode.ESCAPE)) {
                                containingModal.close(EmptyModalResult.INSTANCE);
                            }
                        }
                        return UIEventResponse.processed();
                    });
        }

        return inventoryRow;
    }
}
