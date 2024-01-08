package com.vdn.lampbearer.views.fragments.sidepanel;

import com.vdn.lampbearer.attributes.InventoryAttr;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.game.GameContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Component;
import org.hexworks.zircon.api.component.Fragment;
import org.hexworks.zircon.api.component.VBox;
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


    @NotNull
    @Override
    public Component getRoot() {

        VBox sidePanel = Components.vbox()
                .withPreferredSize(SIDEBAR_WIDTH - 2, SIDEBAR_HEIGHT)
                .withSpacing(1)
                .build();

        sidePanel.addFragment(new SidePanelPlayerAttributesFragment(context.getPlayer().getAttributes()));

        sidePanel.addFragment(
                new SidePanelInventoryFragment(
                        context.getPlayer().findAttribute(InventoryAttr.class).get(),
                        (AbstractItem item) -> {
                            //TODO: Понять как реализовать. Сейчас на экране ничего не появляется
                            // если здесь модальное окно выводить
//                            context.getPlayer().tryUseItem(context, item);
                            log.info("Selected item " + item.getName());
                        })
        );

        return sidePanel;
    }
}
