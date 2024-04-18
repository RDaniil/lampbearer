package com.vdn.lampbearer.views.fragments.sidepanel;

import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.game.GameContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Component;
import org.hexworks.zircon.api.component.ComponentAlignment;
import org.hexworks.zircon.api.component.Fragment;
import org.jetbrains.annotations.NotNull;

import static com.vdn.lampbearer.config.GameConfig.SIDEBAR_HEIGHT;
import static com.vdn.lampbearer.config.GameConfig.SIDEBAR_WIDTH;

/**
 * UI-компонент выбора действия с предметом
 */
@Slf4j
@RequiredArgsConstructor
public class SidePanelFragment implements Fragment {

    private final GameContext context;

    private SidePanelInventoryFragment sidePanelInventoryFragment;
    private SidePanelPlayerAttributesFragment playerAttributesFragment;


    @NotNull
    @Override
    public Component getRoot() {
        var sidePanel = Components.vbox()
                .withPreferredSize(SIDEBAR_WIDTH - 2, SIDEBAR_HEIGHT - 2)
                .withSpacing(1)
                .withAlignmentWithin(context.getSidePanel(), ComponentAlignment.TOP_RIGHT)
                .build();

        playerAttributesFragment =
                new SidePanelPlayerAttributesFragment(context.getPlayer().getAttributes());
        sidePanel.addFragment(playerAttributesFragment);

        sidePanelInventoryFragment = new SidePanelInventoryFragment(
                context.getPlayer().findAttribute(InventoryAttr.class).get());
        sidePanel.addFragment(sidePanelInventoryFragment);

        return sidePanel;
    }


    public void updateContent(GameContext gameContext) {
        sidePanelInventoryFragment.updateContent();
        playerAttributesFragment.updateContent();
    }
}
