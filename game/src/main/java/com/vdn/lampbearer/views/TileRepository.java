package com.vdn.lampbearer.views;

import com.vdn.lampbearer.exception.NoTileFoundException;
import com.vdn.lampbearer.services.config.ConfigBlock;
import com.vdn.lampbearer.services.config.TileGameBlockConfig;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Tile;

import java.util.HashMap;
import java.util.Map;

/**
 * Репозиторий тайлов, возвращает созданные инстансы тайлов, чтобы не плодить миллион объектов
 */
public class TileRepository {

    private final static Map<BlockType, Tile> TYPE_TO_TILE_MAP = new HashMap<>();


    public static void fillTileMap(TileGameBlockConfig tileGameBlockConfig) {
        for (ConfigBlock tile : tileGameBlockConfig.configTileList) {
            TYPE_TO_TILE_MAP.put(tile.getBlockType(), createTileFromConfigBlock(tile));
        }
        TYPE_TO_TILE_MAP.put(BlockType.EMPTY, Tile.empty());
    }

    private static Tile createTileFromConfigBlock(ConfigBlock tile) {
        return Tile.newBuilder().withCharacter(tile.getTile())
                .withBackgroundColor(TileColor.fromString(tile.getBackground()))
                .withForegroundColor(TileColor.fromString(tile.getForeground()))
                .buildCharacterTile();
    }


    public static Tile getTile(BlockType blockType) {
        return TYPE_TO_TILE_MAP.get(blockType);
    }


    public static Tile getTile(Tile tile) {
        try {
            return TYPE_TO_TILE_MAP.values().stream()
                    .filter(t -> t.equals(tile))
                    .findFirst().orElseThrow(() -> new NoTileFoundException(tile));
        } catch (Exception e) {
            throw new NoTileFoundException(tile);
        }
    }


    public static BlockType getBlockType(Tile tile) {
        return TYPE_TO_TILE_MAP.entrySet().stream()
                .filter(entry -> entry.getValue().equals(tile))
                .findFirst().orElseThrow(() -> new NoTileFoundException(tile))
                .getKey();
    }

}
