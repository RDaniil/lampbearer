package com.vdn.lampbearer.views.fragments;

import com.vdn.lampbearer.attributes.InventoryAttr;
import com.vdn.lampbearer.entites.item.AbstractItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Component;
import org.hexworks.zircon.api.component.Fragment;
import org.hexworks.zircon.api.component.VBox;
import org.hexworks.zircon.api.graphics.BoxType;
import org.hexworks.zircon.api.uievent.MouseEventType;
import org.hexworks.zircon.api.uievent.UIEventPhase;
import org.hexworks.zircon.api.uievent.UIEventResponse;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.vdn.lampbearer.config.GameConfig.SIDEBAR_WIDTH;
import static org.hexworks.zircon.api.ComponentDecorations.box;

/**
 * UI-компонент инветаря
 */
@Slf4j
@RequiredArgsConstructor
public class SidePanelInventoryFragment implements Fragment {

    private final InventoryAttr inventory;
    private final Consumer<AbstractItem> itemUseFunction;


    @NotNull
    @Override
    public Component getRoot() {
        VBox inventoryRow = Components.vbox()
                .withPreferredSize(SIDEBAR_WIDTH - 2, inventory.getMaxSize())
                .withDecorations(box(BoxType.SINGLE, "Inventory"))
                .build();

        for (AbstractItem item : inventory.getItems()) {
            inventoryRow.addFragment(new SidePanelInventoryRowFragment(item))
                    .handleMouseEvents(MouseEventType.MOUSE_PRESSED, (mouseEvent, uiEventPhase) -> {
                        if (uiEventPhase.equals(UIEventPhase.CAPTURE)) {
                            itemUseFunction.accept(item);
                        }
                        return UIEventResponse.processed();
                    });
        }

        return inventoryRow;
    }
}
