package com.vdn.lampbearer.factories;

import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.config.ConfigBlock;
import com.vdn.lampbearer.services.config.TileGameBlockConfig;
import com.vdn.lampbearer.views.BlockTypes;
import com.vdn.lampbearer.views.TileRepository;
import org.hexworks.zircon.api.data.Tile;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Фабрика игровых блоков
 */
@Service
public class GameBlockFactory {

    static HashMap<BlockTypes, GameBlock> blockTypesGameBlockMap = new HashMap<>();

    public static void fillGameBlockMap(TileGameBlockConfig tileGameBlockConfig) {
        for (ConfigBlock tile : tileGameBlockConfig.configTileList) {
            blockTypesGameBlockMap.put(tile.getBlockType(),
                    createGameBlock(TileRepository.getTile(tile.getBlockType()),
                            tile.isTransparent(), tile.isWalkable(),
                            tile.getName(), tile.getDescription()));
        }
    }


    public static GameBlock returnGameBlock(BlockTypes blockTypes) {
        GameBlock gameBlock = new GameBlock(blockTypesGameBlockMap.get(blockTypes).getEmptyTile());
        gameBlock.setWalkable(blockTypesGameBlockMap.get(blockTypes).isWalkable());
        gameBlock.setTransparent(blockTypesGameBlockMap.get(blockTypes).isTransparent());
        gameBlock.setName(blockTypesGameBlockMap.get(blockTypes).getName());
        gameBlock.setDescription(blockTypesGameBlockMap.get(blockTypes).getDescription());
        return gameBlock;
    }


    private static GameBlock createGameBlock(Tile tile, boolean isTransparent, boolean isWalkable
            , String blockName, String blockDescription) {
        GameBlock block = new GameBlock(tile);
        block.setWalkable(isWalkable);
        block.setTransparent(isTransparent);
        block.setName(blockName);
        block.setDescription(blockDescription);
        return block;
    }

}
