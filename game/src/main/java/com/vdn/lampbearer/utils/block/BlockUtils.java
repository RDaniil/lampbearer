package com.vdn.lampbearer.utils.block;

import com.vdn.lampbearer.game.world.block.GameBlock;
import org.hexworks.zircon.api.data.Position3D;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static com.vdn.lampbearer.factories.GameBlockFactory.returnGameBlock;
import static com.vdn.lampbearer.views.TileRepository.getBlockType;

/**
 * @author Chizhov D. on 2024.03.10
 */
public class BlockUtils {

    public static Map<Position3D, GameBlock> clone(Map<Position3D, GameBlock> blockMap) {
        if (blockMap.isEmpty()) return Collections.emptyMap();

        return blockMap.entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, entry -> clone(entry.getValue()), (a, b) -> b)
        );
    }


    public static GameBlock clone(GameBlock block) {
        if (block == null) return null;

        GameBlock clone = returnGameBlock(getBlockType(block.getBottomTile()));
        clone.cloneEntities(block.getEntities());
        clone.updateContent();
        return clone;
    }
}
