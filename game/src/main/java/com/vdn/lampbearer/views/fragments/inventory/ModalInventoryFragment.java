package com.vdn.lampbearer.views.fragments.inventory;

import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.config.GameConfig;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.views.fragments.sidepanel.SidePanelInventoryRowFragment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.builder.component.IconBuilder;
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
            addInventoryItemToList(items.get(i), i, inventoryRow);
        }

        containingModal.handleKeyboardEvents(
                KeyboardEventType.KEY_PRESSED,
                (event, uiEventPhase) -> {
                    if (uiEventPhase.equals(UIEventPhase.TARGET)) {
                        if (event.getCode().equals(KeyCode.ESCAPE)) {
                            containingModal.close(EmptyModalResult.INSTANCE);
                        }
                    }
                    return UIEventResponse.processed();
                });

        return inventoryRow;
    }


    private void addInventoryItemToList(AbstractItem item, int itemIndex, VBox inventoryRow) {
        HBox itemOption = Components.hbox()
                .withPreferredSize(width, 1)
                .withDecorations(noDecoration())
                .build();

        itemOption.addComponent(getItemSelectLabel(itemLabels.charAt(itemIndex)));

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

        containingModal.handleKeyboardEvents(
                KeyboardEventType.KEY_PRESSED,
                (event, uiEventPhase) -> {
                    if (uiEventPhase.equals(UIEventPhase.TARGET)) {
                        if (event.getKey().toLowerCase().charAt(0) == itemLabels.charAt(itemIndex)) {
                            containingModal.close(new MenuItemSelected<>(item));
                        }
                    }
                    return UIEventResponse.processed();
                });
    }


    @NotNull
    private static IconBuilder getItemSelectLabel(char itemLabel) {
        return Components.icon()
                .withIcon(Tile.newBuilder().withCharacter(itemLabel)
                        .withBackgroundColor(GameConfig.THEME.getAccentColor())
                        .withForegroundColor(GameConfig.THEME.getSecondaryForegroundColor())
                        .build());
    }
}
