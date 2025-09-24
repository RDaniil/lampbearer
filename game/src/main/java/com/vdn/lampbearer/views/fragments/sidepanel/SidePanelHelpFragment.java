package com.vdn.lampbearer.views.fragments.sidepanel;

import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.entites.interfaces.Printable;
import com.vdn.lampbearer.entites.item.AbstractItem;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.component.*;
import org.hexworks.zircon.api.graphics.BoxType;
import org.hexworks.zircon.api.graphics.StyleSet;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.vdn.lampbearer.config.GameConfig.SIDEBAR_WIDTH;
import static org.hexworks.zircon.api.ComponentDecorations.box;
import static org.hexworks.zircon.api.ComponentDecorations.margin;

/**
 * UI-компонент инветаря
 */
@Slf4j
public class SidePanelHelpFragment implements Fragment {

    private final VBox attributesVbox;

    public SidePanelHelpFragment(){
        attributesVbox = Components.vbox()
                .withPreferredSize(SIDEBAR_WIDTH - 2, 20)
                .withDecorations(margin(0, 0, 0, 1))
                .build();
    }


    @NotNull
    @Override
    public Component getRoot() {
        attributesVbox.addComponent(Components.label().withText("Walk: WASD").build());
        attributesVbox.addComponent(Components.label().withText("Wait: Space").build());
        attributesVbox.addComponent(Components.label().withText("Look: L").build());
        attributesVbox.addComponent(Components.label().withText("Pick up item: F").build());
        attributesVbox.addComponent(Components.label().withText("Interact: F").build());
        attributesVbox.addComponent(Components.label().withText("Shoot revolver: R").build());
        attributesVbox.addComponent(Components.label().withText("Use inventory: U").build());
        attributesVbox.addComponent(Components.label().withText("Rotate revolver:E,Q").build());

        attributesVbox.addComponent(Components.label().withText("Confirm: Enter").build());
        attributesVbox.addComponent(Components.label().withText("Cancel/Exit: Esc").build());
        return attributesVbox;
    }
}
