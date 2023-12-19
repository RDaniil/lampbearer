package com.vdn.lampbearer.views;

import com.vdn.lampbearer.config.GameConfig;
import com.vdn.lampbearer.game.Game;
import com.vdn.lampbearer.game.GameBuilder;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.GameComponents;
import org.hexworks.zircon.api.component.ComponentAlignment;
import org.hexworks.zircon.api.component.renderer.ComponentRenderer;
import org.hexworks.zircon.api.data.Size3D;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.uievent.KeyboardEventType;
import org.hexworks.zircon.api.uievent.Processed;
import org.hexworks.zircon.api.view.base.BaseView;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import static com.vdn.lampbearer.config.GameConfig.SIDEBAR_WIDTH;
import static com.vdn.lampbearer.config.GameConfig.WINDOW_WIDTH;
import static org.hexworks.zircon.api.ComponentDecorations.box;
import static org.hexworks.zircon.api.game.ProjectionMode.TOP_DOWN;

/**
 * UI класс, создает панели, отображаемые в терминале
 * Обрабатывает события ввода (с клавиатуры\мышки)
 */
@Component
public class PlayView extends BaseView {

    public PlayView(@NotNull TileGrid playViewTileGrid, GameBuilder gameBuilder) {
        super(playViewTileGrid, GameConfig.THEME);
        var game = gameBuilder.buildGame(Size3D.create(100, 100, 1));

        var screen = getScreen();
        var panel = Components.panel()
                .withPreferredSize(SIDEBAR_WIDTH, GameConfig.SIDEBAR_HEIGHT)
                .withDecorations(box())
                .build();
        var logArea = Components.logArea()
                .withDecorations(box())
                .withPreferredSize(WINDOW_WIDTH - SIDEBAR_WIDTH, GameConfig.LOG_AREA_HEIGHT)
                .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_RIGHT)
                .build();

        var gameComponent = Components.panel()
                .withPreferredSize(game.getWorld().getVisibleSize().to2DSize())
                .withComponentRenderer(createGameAreaRenderer(game))
                .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
                .build();

        screen.addComponent(panel);
        screen.addComponent(logArea);
        screen.addComponent(gameComponent);

        screen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED, (event, uiEventPhase) -> {
            game.getWorld().update(screen, event, game, logArea);
            return Processed.INSTANCE;
        });
    }

    @NotNull
    private static ComponentRenderer<org.hexworks.zircon.api.component.Panel> createGameAreaRenderer(Game game) {
        return GameComponents.newGameAreaComponentRenderer(game.getWorld(), TOP_DOWN, TileRepository.GROUND);
    }
}
