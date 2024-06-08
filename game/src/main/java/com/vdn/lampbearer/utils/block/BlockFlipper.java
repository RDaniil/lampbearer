package com.vdn.lampbearer.utils.block;

import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.RandomService;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import lombok.AllArgsConstructor;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Tile;

import java.util.*;

import static com.vdn.lampbearer.utils.PositionUtils.*;
import static com.vdn.lampbearer.views.BlockType.*;

/**
 * @author Chizhov D. on 2024.03.08
 */
public class BlockFlipper {

    private static final Map<BlockType, FlippedBlockType> TYPE_TO_FLIPPED_BLOCK_MAP =
            new HashMap<>();

    static {
        TYPE_TO_FLIPPED_BLOCK_MAP.put(UR_CORNER, new FlippedBlockType(BR_CORNER, UL_CORNER));
        TYPE_TO_FLIPPED_BLOCK_MAP.put(BR_CORNER, new FlippedBlockType(UR_CORNER, BL_CORNER));
        TYPE_TO_FLIPPED_BLOCK_MAP.put(UL_CORNER, new FlippedBlockType(BL_CORNER, UR_CORNER));
        TYPE_TO_FLIPPED_BLOCK_MAP.put(BL_CORNER, new FlippedBlockType(UL_CORNER, BR_CORNER));
        TYPE_TO_FLIPPED_BLOCK_MAP.put(UR_CORNER_THICK, new FlippedBlockType(BR_CORNER_THICK, UL_CORNER_THICK));
        TYPE_TO_FLIPPED_BLOCK_MAP.put(BR_CORNER_THICK, new FlippedBlockType(UR_CORNER_THICK, BL_CORNER_THICK));
        TYPE_TO_FLIPPED_BLOCK_MAP.put(UL_CORNER_THICK, new FlippedBlockType(BL_CORNER_THICK, UR_CORNER_THICK));
        TYPE_TO_FLIPPED_BLOCK_MAP.put(BL_CORNER_THICK, new FlippedBlockType(UL_CORNER_THICK, BR_CORNER_THICK));
        TYPE_TO_FLIPPED_BLOCK_MAP.put(D_WALL_THICK_TO_SLIM_JOINT, new FlippedBlockType(U_WALL_THICK_TO_SLIM_JOINT, D_WALL_THICK_TO_SLIM_JOINT));
        TYPE_TO_FLIPPED_BLOCK_MAP.put(U_WALL_THICK_TO_SLIM_JOINT, new FlippedBlockType(D_WALL_THICK_TO_SLIM_JOINT, U_WALL_THICK_TO_SLIM_JOINT));
        TYPE_TO_FLIPPED_BLOCK_MAP.put(L_WALL_THICK_TO_SLIM_JOINT, new FlippedBlockType(L_WALL_THICK_TO_SLIM_JOINT, R_WALL_THICK_TO_SLIM_JOINT));
        TYPE_TO_FLIPPED_BLOCK_MAP.put(R_WALL_THICK_TO_SLIM_JOINT, new FlippedBlockType(R_WALL_THICK_TO_SLIM_JOINT, L_WALL_THICK_TO_SLIM_JOINT));
        TYPE_TO_FLIPPED_BLOCK_MAP.put(D_WALL_THICK_JOINT, new FlippedBlockType(U_WALL_THICK_JOINT, D_WALL_THICK_JOINT));
        TYPE_TO_FLIPPED_BLOCK_MAP.put(U_WALL_THICK_JOINT, new FlippedBlockType(D_WALL_THICK_JOINT, U_WALL_THICK_JOINT));
        TYPE_TO_FLIPPED_BLOCK_MAP.put(L_WALL_THICK_JOINT, new FlippedBlockType(L_WALL_THICK_JOINT, R_WALL_THICK_JOINT));
        TYPE_TO_FLIPPED_BLOCK_MAP.put(R_WALL_THICK_JOINT, new FlippedBlockType(R_WALL_THICK_JOINT, L_WALL_THICK_JOINT));
    }

    public static Map<Position3D, GameBlock> flip(Map<Position3D, GameBlock> blockMap) {
        return flip(blockMap, Direction.getRandom());
    }


    public static Map<Position3D, GameBlock> flip(Map<Position3D, GameBlock> blockMap,
                                                  Direction direction) {
        if (blockMap.isEmpty()) return Collections.emptyMap();

        Map<Position3D, GameBlock> flippedBlockMap = new HashMap<>();

        Axis axis = direction.getAxis(blockMap.keySet());

        for (var positionToBlock : blockMap.entrySet()) {
            GameBlock sourceBlock = positionToBlock.getValue();
            Tile bottomTile = sourceBlock.getBottomTile();
            BlockType blockType = TileRepository.getBlockType(bottomTile);
            FlippedBlockType flippedBlockType = TYPE_TO_FLIPPED_BLOCK_MAP.get(blockType);
            if (flippedBlockType != null) {
                blockType = flippedBlockType.getFlipped(direction);
            }

            GameBlock block = GameBlockFactory.returnGameBlock(blockType);
            block.cloneEntities(sourceBlock.getEntities());
            block.updateContent();

            flippedBlockMap.put(direction.getNewPosition(positionToBlock.getKey(), axis), block);
        }

        return flippedBlockMap;
    }


    public enum Direction {
        DOWN {
            @Override
            Axis getAxis(Set<Position3D> positions) {
                int maxY = getMaxY(positions);
                int minY = getMinY(positions);
                return new Axis(maxY, maxY - minY);
            }


            @Override
            Position3D getNewPosition(Position3D position, Axis axis) {
                var newY = position.getY() + (axis.coordinate - position.getY()) * 2 - axis.offset;
                return Position3D.create(position.getX(), newY, 0);
            }
        },
        LEFT {
            @Override
            Axis getAxis(Set<Position3D> positions) {
                int maxX = getMaxX(positions);
                int minX = getMinX(positions);
                return new Axis(minX, maxX - minX);
            }


            @Override
            Position3D getNewPosition(Position3D position, Axis axis) {
                var newX = position.getX() - (position.getX() - axis.coordinate) * 2 + axis.offset;
                return Position3D.create(newX, position.getY(), 0);
            }
        },
        RIGHT {
            @Override
            Axis getAxis(Set<Position3D> positions) {
                int maxX = getMaxX(positions);
                int minX = getMinX(positions);
                return new Axis(maxX, maxX - minX);
            }


            @Override
            Position3D getNewPosition(Position3D position, Axis axis) {
                var newX = position.getX() + (axis.coordinate - position.getX()) * 2 - axis.offset;
                return Position3D.create(newX, position.getY(), 0);
            }
        },
        UP {
            @Override
            Axis getAxis(Set<Position3D> positions) {
                int maxY = getMaxY(positions);
                int minY = getMinY(positions);
                return new Axis(minY, maxY - minY);
            }


            @Override
            Position3D getNewPosition(Position3D position, Axis axis) {
                var newY = position.getY() - (position.getY() - axis.coordinate) * 2 + axis.offset;
                return Position3D.create(position.getX(), newY, 0);
            }
        };


        public static Direction getRandom() {
            Direction[] values = Direction.values();
            return values[RandomService.getRandom(0, values.length - 1)];
        }


        /**
         * @param positions все позиции объекта
         * @return ось отражения
         */
        abstract Axis getAxis(Set<Position3D> positions);

        /**
         * @param position текущая позиция
         * @param axis     ось отражения
         * @return новая позиция
         */
        abstract Position3D getNewPosition(Position3D position, Axis axis);
    }

    @AllArgsConstructor
    private static class Axis {
        /**
         * Координата оси отражения
         */
        private final int coordinate;
        /**
         * Смещение от левого верхнего угла до оси отражения
         */
        private final int offset;
    }

    @AllArgsConstructor
    private static class FlippedBlockType {

        private final BlockType horizontal;
        private final BlockType vertical;


        private BlockType getFlipped(Direction direction) {
            switch (direction) {
                case DOWN:
                case UP:
                    return horizontal;
                case LEFT:
                case RIGHT:
                    return vertical;
                default:
                    throw new NoSuchElementException();
            }
        }
    }
}
