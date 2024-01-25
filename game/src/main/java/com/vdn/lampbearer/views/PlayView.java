package com.vdn.lampbearer.views;

import com.vdn.lampbearer.config.GameConfig;
import com.vdn.lampbearer.game.Game;
import com.vdn.lampbearer.game.GameBuilder;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.GameComponents;
import org.hexworks.zircon.api.builder.graphics.LayerBuilder;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.component.ComponentAlignment;
import org.hexworks.zircon.api.component.LogArea;
import org.hexworks.zircon.api.component.Panel;
import org.hexworks.zircon.api.component.renderer.ComponentRenderer;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.data.Size3D;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.grid.TileGrid;
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
@Getter
@Slf4j
public class PlayView extends BaseView {

    public final Game game;
    private final LogArea logArea;
    private final Panel sidePanel;


    @SneakyThrows
    public PlayView(@NotNull TileGrid playViewTileGrid, GameBuilder gameBuilder) {
        super(playViewTileGrid, GameConfig.THEME);
        this.game = gameBuilder.buildGame(Size3D.create(100, 100, 1));

        //Нужен чтобы скрывать артефакт библиотеки: при вызове detach() копонента на один кадр
        // переносится в левый верхний угол экрана. Проблема связана с многопоточкой, пока не
        // понятнок как ее нормально решать, оставив при этом "анимации" передвижения
        Layer firstLineLayer = LayerBuilder.newBuilder()
                .withOffset(0, 0)
                .withSize(Size.create(WINDOW_WIDTH, 1))
                .withFiller(Tile.newBuilder()
                        .withBackgroundColor(TileColor.fromString("#FFFFFF"))
                        .withCharacter(' ')
                        .build())
                .build();

        var screen = getScreen();
        screen.addLayer(firstLineLayer);
        this.sidePanel = Components.panel()
                .withPosition(0, 1)
                .withPreferredSize(SIDEBAR_WIDTH, GameConfig.SIDEBAR_HEIGHT - 1)
                .withDecorations(box())
                .withUpdateOnAttach(true)
                .build();

        this.logArea = Components.logArea()
                .withDecorations(box())
                .withPreferredSize(WINDOW_WIDTH - SIDEBAR_WIDTH, GameConfig.LOG_AREA_HEIGHT)
                .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_RIGHT)
                .build();

        var gameComponent = Components.panel()
                .withPosition(0, 1)
                .withPreferredSize(
                        game.getWorld().getVisibleSize().to2DSize().getWidth(),
                        game.getWorld().getVisibleSize().to2DSize().getHeight() - 1)
                .withComponentRenderer(createGameAreaRenderer(game))
                .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
                .build();

        screen.addComponent(sidePanel);
        screen.addComponent(logArea);
        screen.addComponent(gameComponent);
    }

    @NotNull
    private static ComponentRenderer<org.hexworks.zircon.api.component.Panel> createGameAreaRenderer(Game game) {
        return GameComponents.newGameAreaComponentRenderer(game.getWorld(), TOP_DOWN,
                TileRepository.getTile(BlockTypes.GROUND));
    }
}
