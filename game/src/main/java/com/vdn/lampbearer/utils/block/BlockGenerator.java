package com.vdn.lampbearer.utils.block;

import com.vdn.lampbearer.exception.PlaceTryLimitExceededException;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.RandomService;
import com.vdn.lampbearer.views.BlockType;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;

import java.util.*;

/**
 * @author Chizhov D. on 2024.03.11
 */
public class BlockGenerator extends AbstractObjectGenerator {

    private final Map<Position3D, GameBlock> blockMap = new HashMap<>();

    public BlockGenerator(Size3D worldSize) {
        super(worldSize);
}


    @Override
    protected void addGeneratedObject(BlockType objectType, Position3D position) {
        GameBlock block = GameBlockFactory.returnGameBlock(objectType);
        blockMap.put(position, block);
    }

    /**
     * @return позицию, если существует, иначе null
     */
    @Override
    protected Position3D getSuitablePosition() {
        Position3D start = null;

        try {
            int tryNumber = 0;
            while (start == null || getIsOccupied()[start.getX()][start.getY()]) {
                start = Position3D.create(
                        RandomService.getRandom(0, getMaxX()),
                        RandomService.getRandom(0, getMaxY()),
                        0
                );

                if (++tryNumber > getMaxBlockNumber()) throw new PlaceTryLimitExceededException();
            }
        } catch (PlaceTryLimitExceededException ignored) {
            if (isWorldFull()) return null;

            for (int i = 0; i <= getMaxX(); i++) {
                for (int j = 0; j <= getMaxY(); j++) {
                    if (getIsOccupied()[i][j]) continue;

                    return Position3D.create(i, j, 0);
                }
            }
        }

        return start;
    }

    /**
     * Заполнит пустые клетки блоками GROUND
     *
     * @return карту позиция-блок
     */
    public Map<Position3D, GameBlock> getResult() {
        if (isWorldFull()) return blockMap;

        for (int i = 0; i <= getMaxX(); i++) {
            for (int j = 0; j <= getMaxY(); j++) {
                if (getIsOccupied()[i][j]) continue;

                blockMap.put(
                        Position3D.create(i, j, 0),
                        GameBlockFactory.returnGameBlock(BlockType.GROUND)
                );
                getIsOccupied()[i][j] = true;
            }
        }
        return blockMap;
    }


    private boolean isWorldFull() {
        return blockMap.size() >= getMaxBlockNumber();
    }
}
