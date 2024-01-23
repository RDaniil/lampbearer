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

    static Map<BlockTypes, Tile> blockTypesTileMap = new HashMap<>();


    public static void fillTileMap(TileGameBlockConfig tileGameBlockConfig) {
        for (ConfigBlock tile : tileGameBlockConfig.configTileList) {
            blockTypesTileMap.put(tile.getBlockType(), createTileFromConfigBlock(tile));
        }
        blockTypesTileMap.put(BlockTypes.EMPTY, Tile.empty());
    }


    private static Tile createTileFromConfigBlock(ConfigBlock tile) {
        return Tile.newBuilder().withCharacter(tile.getTile())
                .withBackgroundColor(TileColor.fromString(tile.getBackground()))
                .withForegroundColor(TileColor.fromString(tile.getForeground()))
                .buildCharacterTile();
    }


    public static Tile getTile(BlockTypes blockTypes) {
        return blockTypesTileMap.get(blockTypes);
    }


    public static Tile getTile(Tile tile) {
        try {
            return blockTypesTileMap.values().stream()
                    .filter(t -> t.equals(tile))
                    .findFirst().orElseThrow(() -> new NoTileFoundException(tile));
        } catch (Exception e) {
            throw new NoTileFoundException(tile);
        }
    }


    public static BlockTypes getBlockType(Tile tile) {
        return blockTypesTileMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(tile))
                .findFirst().orElseThrow(() -> new NoTileFoundException(tile))
                .getKey();
    }

}
