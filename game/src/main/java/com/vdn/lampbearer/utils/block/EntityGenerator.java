package com.vdn.lampbearer.utils.block;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.exception.PlaceTryLimitExceededException;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.RandomService;
import com.vdn.lampbearer.views.BlockType;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;

import java.util.*;

/**
 * @author Chizhov D. on 2024.03.11
 */
public class EntityGenerator extends AbstractObjectGenerator {

    /**
     * Отступ от краев карты для выбора спавна сущностей
     */
    private final static int SPAWN_PADDING = 5;

    private final Map<Position3D, AbstractEntity> entityMap = new HashMap<>();
    private final Map<Position3D, GameBlock> positionToBlockMap;

    public EntityGenerator(Size3D worldSize, Map<Position3D, GameBlock> positionToBlockMap) {
        super(worldSize);
        this.positionToBlockMap = positionToBlockMap;
    }

    @Override
    protected void addGeneratedObject(BlockType objectType, Position3D position) {
        AbstractEntity entity = AbstractEntity.create(objectType, position);
        entityMap.put(position, entity);
    }


    /**
     * @return позицию, если существует, иначе null
     */
    @Override
    protected Position3D getSuitablePosition() {
        Position3D start = null;

        try {
            int tryNumber = 0;
            while (start == null || !isWorldBlockSuitableForSpawn(start) || getIsOccupied()[start.getX()][start.getY()]) {
                start = Position3D.create(
                        RandomService.getRandom(SPAWN_PADDING, getMaxX() - SPAWN_PADDING),
                        RandomService.getRandom(SPAWN_PADDING, getMaxY() - SPAWN_PADDING),
                        0
                );

                if (++tryNumber > getMaxBlockNumber()) throw new PlaceTryLimitExceededException();
            }
        } catch (PlaceTryLimitExceededException ignored) {

            for (int i = SPAWN_PADDING; i <= getMaxX() - SPAWN_PADDING; i++) {
                for (int j = SPAWN_PADDING; j <= getMaxY() - SPAWN_PADDING; j++) {
                    if (getIsOccupied()[i][j]) continue;

                    return Position3D.create(i, j, 0);
                }
            }
        }

        return start;
    }

    private boolean isWorldBlockSuitableForSpawn(Position3D start) {
        return positionToBlockMap.get(start).isWalkable() && !positionToBlockMap.get(start).hasEntities();
    }

    public Map<Position3D, AbstractEntity> getResult(){
        return entityMap;
    }

}
