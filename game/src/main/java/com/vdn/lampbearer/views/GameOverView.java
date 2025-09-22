package com.vdn.lampbearer.views;

import com.vdn.lampbearer.config.GameConfig;
import com.vdn.lampbearer.constants.GameOverResult;
import com.vdn.lampbearer.exception.GameOverException;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.views.fragments.GameOverFragment;
import lombok.RequiredArgsConstructor;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.builder.component.ModalBuilder;
import org.hexworks.zircon.api.component.ComponentAlignment;
import org.hexworks.zircon.api.graphics.BoxType;
import org.jetbrains.annotations.NotNull;

import static org.hexworks.zircon.api.ComponentDecorations.box;

@RequiredArgsConstructor
public class GameOverView {
    private final GameContext context;
    private final GameOverResult gameOverResult;

    public void show() {
        var panel = Components.panel()
                .withPreferredSize(40, 20)
                .withDecorations(box(BoxType.DOUBLE, getTitleText()))
                .build();

        GameOverFragment gameOverFragment = new GameOverFragment(context);
        panel.addFragment(gameOverFragment);

        var modal = ModalBuilder.newBuilder()
                .withPreferredSize(panel.getSize())
                .withComponent(panel)
                .withAlignmentWithin(context.getScreen(), ComponentAlignment.CENTER)
                .withTileset(GameConfig.getAppConfig().getDefaultTileset())
                .withColorTheme(GameConfig.THEME)
                .build();


        context.getScreen().openModal(modal);
    }

    @NotNull
    private String getTitleText() {
        if(GameOverResult.PLAYER_DEAD.equals(gameOverResult)){
            return "Game over";
        } else {
            return "Village is saved!";
        }
    }
}
