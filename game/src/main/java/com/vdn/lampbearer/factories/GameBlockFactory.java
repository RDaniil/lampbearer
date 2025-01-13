package com.vdn.lampbearer.factories;

import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.config.ConfigBlock;
import com.vdn.lampbearer.services.config.TileGameBlockConfig;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import org.hexworks.zircon.api.data.Tile;

import java.util.HashMap;

/**
 * Фабрика игровых блоков
 */
public class GameBlockFactory {

    private final static HashMap<BlockType, GameBlock> TYPE_TO_BLOCK_MAP = new HashMap<>();


    public static void fillGameBlockMap(TileGameBlockConfig tileGameBlockConfig) {
        for (ConfigBlock tile : tileGameBlockConfig.configTileList) {
            TYPE_TO_BLOCK_MAP.put(
                    tile.getBlockType(),
                    createGameBlock(
                            tile.getBlockType(),
                            tile.isTransparent(),
                            tile.isWalkable(),
                            tile.getName(),
                            tile.getDescription()
                    )
            );
        }
    }


    public static GameBlock returnGameBlock(BlockType blockType) {
        if (BlockType.EMPTY.equals(blockType)) return GameBlock.createEmpty();

        GameBlock block = TYPE_TO_BLOCK_MAP.get(blockType);
        GameBlock gameBlock = new GameBlock(block.getEmptyTile());
        gameBlock.setWalkable(block.isWalkable());
        gameBlock.setTransparent(block.isTransparent());
        gameBlock.setName(block.getName());
        gameBlock.setDescription(block.getDescription());
        return gameBlock;
    }


    public static GameBlock returnGameBlock(Tile tile) {
        return returnGameBlock(TileRepository.getBlockType(tile));
    }


    private static GameBlock createGameBlock(BlockType blockType,
                                             boolean isTransparent,
                                             boolean isWalkable,
                                             String blockName,
                                             String blockDescription) {
        GameBlock block = new GameBlock(TileRepository.getTile(blockType));
        block.setWalkable(isWalkable);
        block.setTransparent(isTransparent);
        block.setName(blockName);
        block.setDescription(blockDescription);

        return block;
    }
}
