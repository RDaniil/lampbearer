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
    public static Tile UNSEEN = Tile.newBuilder()
            .withCharacter(' ')
            .withBackgroundColor(GameColorConstants.DEFAULT_BACKGROUND)
            .withForegroundColor(GameColorConstants.DEFAULT_BACKGROUND)
            .buildCharacterTile();

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

    public static Tile H_CLOSED_DOOR = Tile.newBuilder()
            .withCharacter(GameCharacterConstants.H_WALL_THIN)
            .withBackgroundColor(GameColorConstants.DEFAULT_BACKGROUND)
            .withForegroundColor(GameColorConstants.DOOR_COLOR)
            .buildCharacterTile();

    public static Tile V_CLOSED_DOOR = Tile.newBuilder()
            .withCharacter(GameCharacterConstants.V_WALL_THIN)
            .withBackgroundColor(GameColorConstants.DEFAULT_BACKGROUND)
            .withForegroundColor(GameColorConstants.DOOR_COLOR)
            .buildCharacterTile();

    public static Tile OPENED_DOOR = Tile.newBuilder()
            .withCharacter(GameCharacterConstants.OPENED_DOOR)
            .withBackgroundColor(GameColorConstants.DEFAULT_BACKGROUND)
            .withForegroundColor(GameColorConstants.DOOR_COLOR)
            .buildCharacterTile();

    public static final Tile UR_CORNER = Tile.newBuilder()
            .withCharacter(GameCharacterConstants.UR_CORNER)
            .withBackgroundColor(GameColorConstants.DEFAULT_BACKGROUND)
            .withForegroundColor(GameColorConstants.FLOOR_FOREGROUND)
            .buildCharacterTile();
    public static final Tile BR_CORNER = Tile.newBuilder()
            .withCharacter(GameCharacterConstants.BR_CORNER)
            .withBackgroundColor(GameColorConstants.DEFAULT_BACKGROUND)
            .withForegroundColor(GameColorConstants.FLOOR_FOREGROUND)
            .buildCharacterTile();
    public static final Tile UL_CORNER = Tile.newBuilder()
            .withCharacter(GameCharacterConstants.UL_CORNER)
            .withBackgroundColor(GameColorConstants.DEFAULT_BACKGROUND)
            .withForegroundColor(GameColorConstants.FLOOR_FOREGROUND)
            .buildCharacterTile();
    public static final Tile BL_CORNER = Tile.newBuilder()
            .withCharacter(GameCharacterConstants.BL_CORNER)
            .withBackgroundColor(GameColorConstants.DEFAULT_BACKGROUND)
            .withForegroundColor(GameColorConstants.FLOOR_FOREGROUND)
            .buildCharacterTile();
    public static final Tile H_WALL_THICK = Tile.newBuilder()
            .withCharacter(GameCharacterConstants.H_WALL_THICK)
            .withBackgroundColor(GameColorConstants.DEFAULT_BACKGROUND)
            .withForegroundColor(GameColorConstants.FLOOR_FOREGROUND)
            .buildCharacterTile();
    public static final Tile V_WALL_THICK = Tile.newBuilder()
            .withCharacter(GameCharacterConstants.V_WALL_THICK)
            .withBackgroundColor(GameColorConstants.DEFAULT_BACKGROUND)
            .withForegroundColor(GameColorConstants.FLOOR_FOREGROUND)
            .buildCharacterTile();

    public static Tile FIRST_AID_KIT = Tile.newBuilder()
            .withCharacter(GameCharacterConstants.FIRST_AID_KIT)
            .withBackgroundColor(GameColorConstants.DEFAULT_BACKGROUND)
            .withForegroundColor(GameColorConstants.FIRST_AID_KIT)
            .buildCharacterTile();
}
