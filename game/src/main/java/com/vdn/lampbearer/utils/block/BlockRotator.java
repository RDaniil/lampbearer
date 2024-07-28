package com.vdn.lampbearer.utils.block;

import com.vdn.lampbearer.attributes.arrangement.Arrangement;
import com.vdn.lampbearer.attributes.arrangement.HorizontalArrangement;
import com.vdn.lampbearer.attributes.arrangement.VerticalArrangement;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.objects.Door;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.RandomService;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import lombok.AllArgsConstructor;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Tile;

import java.util.*;

import static com.vdn.lampbearer.utils.PositionUtils.getMaxX;
import static com.vdn.lampbearer.utils.PositionUtils.getMaxY;
import static com.vdn.lampbearer.views.BlockType.*;

/**
 * @author Chizhov D. on 2024.03.09
 */
public class BlockRotator {

    private static final Map<BlockType, RotatedBlockType> TYPE_TO_ROTATED_BLOCK_MAP =
            new HashMap<>();

    static {
        TYPE_TO_ROTATED_BLOCK_MAP.put(H_GLASS, new RotatedBlockType(V_GLASS, H_GLASS, V_GLASS));
        TYPE_TO_ROTATED_BLOCK_MAP.put(V_GLASS, new RotatedBlockType(H_GLASS, V_GLASS, H_GLASS));
        TYPE_TO_ROTATED_BLOCK_MAP.put(H_CLOSED_DOOR, new RotatedBlockType(V_CLOSED_DOOR, H_CLOSED_DOOR, V_CLOSED_DOOR));
        TYPE_TO_ROTATED_BLOCK_MAP.put(V_CLOSED_DOOR, new RotatedBlockType(H_CLOSED_DOOR, V_CLOSED_DOOR, H_CLOSED_DOOR));
        TYPE_TO_ROTATED_BLOCK_MAP.put(H_WALL, new RotatedBlockType(V_WALL, H_WALL, V_WALL));
        TYPE_TO_ROTATED_BLOCK_MAP.put(V_WALL, new RotatedBlockType(H_WALL, V_WALL, H_WALL));
        TYPE_TO_ROTATED_BLOCK_MAP.put(H_WALL_THICK, new RotatedBlockType(V_WALL_THICK, H_WALL_THICK, V_WALL_THICK));
        TYPE_TO_ROTATED_BLOCK_MAP.put(V_WALL_THICK, new RotatedBlockType(H_WALL_THICK, V_WALL_THICK, H_WALL_THICK));
        TYPE_TO_ROTATED_BLOCK_MAP.put(UR_CORNER, new RotatedBlockType(BR_CORNER, BL_CORNER, UL_CORNER));
        TYPE_TO_ROTATED_BLOCK_MAP.put(BR_CORNER, new RotatedBlockType(BL_CORNER, UL_CORNER, UR_CORNER));
        TYPE_TO_ROTATED_BLOCK_MAP.put(UL_CORNER, new RotatedBlockType(UR_CORNER, BR_CORNER, BL_CORNER));
        TYPE_TO_ROTATED_BLOCK_MAP.put(BL_CORNER, new RotatedBlockType(UL_CORNER, UR_CORNER, BR_CORNER));
        TYPE_TO_ROTATED_BLOCK_MAP.put(UR_CORNER_THICK, new RotatedBlockType(BR_CORNER_THICK, BL_CORNER_THICK, UL_CORNER_THICK));
        TYPE_TO_ROTATED_BLOCK_MAP.put(BR_CORNER_THICK, new RotatedBlockType(BL_CORNER_THICK, UL_CORNER_THICK, UR_CORNER_THICK));
        TYPE_TO_ROTATED_BLOCK_MAP.put(UL_CORNER_THICK, new RotatedBlockType(UR_CORNER_THICK, BR_CORNER_THICK, BL_CORNER_THICK));
        TYPE_TO_ROTATED_BLOCK_MAP.put(BL_CORNER_THICK, new RotatedBlockType(UL_CORNER_THICK, UR_CORNER_THICK, BR_CORNER_THICK));
        TYPE_TO_ROTATED_BLOCK_MAP.put(D_WALL_THICK_TO_SLIM_JOINT, new RotatedBlockType(L_WALL_THICK_TO_SLIM_JOINT, U_WALL_THICK_TO_SLIM_JOINT, R_WALL_THICK_TO_SLIM_JOINT));
        TYPE_TO_ROTATED_BLOCK_MAP.put(U_WALL_THICK_TO_SLIM_JOINT, new RotatedBlockType(R_WALL_THICK_TO_SLIM_JOINT, D_WALL_THICK_TO_SLIM_JOINT, L_WALL_THICK_TO_SLIM_JOINT));
        TYPE_TO_ROTATED_BLOCK_MAP.put(L_WALL_THICK_TO_SLIM_JOINT, new RotatedBlockType(U_WALL_THICK_TO_SLIM_JOINT, R_WALL_THICK_TO_SLIM_JOINT, D_WALL_THICK_TO_SLIM_JOINT));
        TYPE_TO_ROTATED_BLOCK_MAP.put(R_WALL_THICK_TO_SLIM_JOINT, new RotatedBlockType(D_WALL_THICK_TO_SLIM_JOINT, L_WALL_THICK_TO_SLIM_JOINT, U_WALL_THICK_TO_SLIM_JOINT));
    }

    public static Map<Position3D, GameBlock> rotate(Map<Position3D, GameBlock> blockMap) {
        Angle angle = Angle.getRandom();
        return angle != null ? rotate(blockMap, angle) : BlockUtils.clone(blockMap);
    }


    public static Map<Position3D, GameBlock> rotate(Map<Position3D, GameBlock> blockMap,
                                                    Angle direction) {
        if (blockMap.isEmpty()) return Collections.emptyMap();

        Map<Position3D, GameBlock> flippedBlockMap = new HashMap<>();

        Position3D offset = direction.getOffset(blockMap.keySet());

        for (var positionToBlock : blockMap.entrySet()) {
            GameBlock sourceBlock = positionToBlock.getValue();
            Tile bottomTile = sourceBlock.getBottomTile();
            BlockType blockType = TileRepository.getBlockType(bottomTile);
            RotatedBlockType rotatedBlockType = TYPE_TO_ROTATED_BLOCK_MAP.get(blockType);
            if (rotatedBlockType != null) {
                blockType = rotatedBlockType.getRotated(direction);
            }

            GameBlock block = GameBlockFactory.returnGameBlock(blockType);
            block.cloneEntities(sourceBlock.getEntities());

            for (AbstractEntity entity : block.getEntities()) {
                blockType = TileRepository.getBlockType(entity.getTile());
                rotatedBlockType = TYPE_TO_ROTATED_BLOCK_MAP.get(blockType);
                if (rotatedBlockType != null) {
                    blockType = rotatedBlockType.getRotated(direction);
                    entity.setTile(TileRepository.getTile(blockType));

                    if (entity instanceof Door) {
                        entity.removeAttribute(Arrangement.class);
                        if (BlockType.H_CLOSED_DOOR.equals(blockType)) {
                            entity.getAttributes().add(HorizontalArrangement.getInstance());
                        } else if (BlockType.V_CLOSED_DOOR.equals(blockType)) {
                            entity.getAttributes().add(VerticalArrangement.getInstance());
                        }
                    }
                }
            }

            block.updateContent();

            flippedBlockMap.put(direction.getNewPosition(positionToBlock.getKey(), offset), block);
        }

        return flippedBlockMap;
    }


    public enum Angle {
        CLOCK_90 {
            @Override
            Position3D getOffset(Set<Position3D> positions) {
                return Position3D.create(getMaxY(positions), 0, 0);
            }


            @Override
            Position3D getNewPosition(Position3D position, Position3D offset) {
                return Position3D.create(-position.getY(), position.getX(), 0)
                        .withRelative(offset);
            }
        },
        CLOCK_180 {
            @Override
            Position3D getOffset(Set<Position3D> positions) {
                int maxX = getMaxX(positions);
                int maxY = getMaxY(positions);
                return Position3D.create(maxX, maxY, 0);
            }


            @Override
            Position3D getNewPosition(Position3D position, Position3D offset) {
                return Position3D.create(-position.getX(), -position.getY(), 0)
                        .withRelative(offset);
            }
        },
        CLOCK_270 {
            @Override
            Position3D getOffset(Set<Position3D> positions) {
                return Position3D.create(0, getMaxX(positions), 0);
            }


            @Override
            Position3D getNewPosition(Position3D position, Position3D offset) {
                return Position3D.create(position.getY(), -position.getX(), 0)
                        .withRelative(offset);
            }
        };


        public static Angle getRandom() {
            Angle[] values = Angle.values();
            int index = RandomService.getRandom(0, values.length);
            return index < values.length ? values[index] : null;
        }


        /**
         * @param positions все позиции объекта
         * @return смещение к левому верхнему углу
         */
        abstract Position3D getOffset(Set<Position3D> positions);

        /**
         * @param position текущая позиция
         * @param offset   ось отражения
         * @return новая позиция
         */
        abstract Position3D getNewPosition(Position3D position, Position3D offset);
    }

    @AllArgsConstructor
    private static class RotatedBlockType {

        private final BlockType clock90;
        private final BlockType clock180;
        private final BlockType clock270;


        private BlockType getRotated(Angle direction) {
            switch (direction) {
                case CLOCK_90:
                    return clock90;
                case CLOCK_180:
                    return clock180;
                case CLOCK_270:
                    return clock270;
                default:
                    throw new NoSuchElementException();
            }
        }
    }
}
