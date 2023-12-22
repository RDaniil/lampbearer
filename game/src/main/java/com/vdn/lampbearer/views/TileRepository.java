package com.vdn.lampbearer.views;

import com.vdn.lampbearer.constants.GameCharacterConstants;
import com.vdn.lampbearer.constants.GameColorConstants;
import org.hexworks.zircon.api.data.Tile;

/**
 * Репозиторий тайлов, возвращает созданные инстансы тайлов, чтобы не плодить миллион объектов
 */
public class TileRepository {

    public static final Tile PLAYER = Tile.newBuilder()
            .withCharacter(GameCharacterConstants.PLAYER)
            .withBackgroundColor(GameColorConstants.DEFAULT_BACKGROUND)
            .withForegroundColor(GameColorConstants.PLAYER_COLOR)
            .buildCharacterTile();

    public static final Tile ROCK = Tile.newBuilder()
            .withCharacter(GameCharacterConstants.ROCK)
            .withBackgroundColor(GameColorConstants.DEFAULT_BACKGROUND)
            .withForegroundColor(GameColorConstants.FLOOR_FOREGROUND)
            .buildCharacterTile();

    public static Tile EMPTY = Tile.empty();
    public static Tile GROUND = Tile.newBuilder()
            .withCharacter(GameCharacterConstants.GROUND)
            .withBackgroundColor(GameColorConstants.DEFAULT_BACKGROUND)
            .withForegroundColor(GameColorConstants.FLOOR_FOREGROUND)
            .buildCharacterTile();

    public static Tile GRASS = Tile.newBuilder()
            .withCharacter(GameCharacterConstants.GRASS)
            .withBackgroundColor(GameColorConstants.DEFAULT_BACKGROUND)
            .withForegroundColor(GameColorConstants.FLOOR_FOREGROUND)
            .buildCharacterTile();

    public static final Tile SIMPLE_ZOMBIE = Tile.newBuilder()
            .withCharacter(GameCharacterConstants.SIMPLE_ZOMBIE)
            .withBackgroundColor(GameColorConstants.DEFAULT_BACKGROUND)
            .withForegroundColor(GameColorConstants.SIMPLE_ZOMBIE_COLOR)
            .buildCharacterTile();
}
