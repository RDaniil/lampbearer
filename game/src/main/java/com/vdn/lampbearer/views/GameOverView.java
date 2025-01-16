package com.vdn.lampbearer.views;

import com.vdn.lampbearer.config.GameConfig;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.views.fragments.GameOverFragment;
import lombok.RequiredArgsConstructor;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.builder.component.ModalBuilder;
import org.hexworks.zircon.api.component.ComponentAlignment;
import org.hexworks.zircon.api.graphics.BoxType;

import static org.hexworks.zircon.api.ComponentDecorations.box;

@RequiredArgsConstructor
public class GameOverView {
    private final GameContext context;

    public void show() {
        var panel = Components.panel()
                .withPreferredSize(60, 20)
                .withDecorations(box(BoxType.DOUBLE, "Game over"))
                .build();

        GameOverFragment gameOverFragment = new GameOverFragment(context);

        var modal = ModalBuilder.newBuilder()
                .withPreferredSize(panel.getSize())
                .withComponent(panel)
                .withAlignmentWithin(context.getScreen(), ComponentAlignment.CENTER)
                .withTileset(GameConfig.getAppConfig().getDefaultTileset())
                .withColorTheme(GameConfig.THEME)
                .build();

        panel.addFragment(gameOverFragment);

        context.getScreen().openModal(modal);
    }
}
