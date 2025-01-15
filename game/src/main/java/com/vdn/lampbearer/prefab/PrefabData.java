package com.vdn.lampbearer.prefab;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.objects.Door;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.RandomService;
import com.vdn.lampbearer.services.spawn.Spawner;
import com.vdn.lampbearer.utils.PositionUtils;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import lombok.Getter;
import org.hexworks.zircon.api.data.Position3D;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Chizhov D. on 2024.03.10
 */
@Getter
public class PrefabData {

    public static final PrefabData EMPTY = new PrefabData(
            null,
            Collections.singletonMap(Position3D.defaultPosition(), GameBlock.createEmpty())
    );

    private static final Set<BlockType> ENTITY_PLACEMENT_BLOCK_TYPE = new HashSet<>();

    static {
        ENTITY_PLACEMENT_BLOCK_TYPE.add(BlockType.GROUND);
    }

    private static final int MARGIN = 3;

    private final Prefab prefab;
    private final Map<Position3D, GameBlock> blockMap;
    private final int sizeX;
    private final int sizeY;
    private final Position3D center;
    private final Position3D offset;
    private final Position3D leftTop;
    private final Position3D rightBot;


    public PrefabData(Prefab prefab, Map<Position3D, GameBlock> blockMap) {
        this(prefab, blockMap, Position3D.defaultPosition());
    }


    public PrefabData(Prefab prefab, Map<Position3D, GameBlock> blockMap, Position3D offset) {
        this.prefab = prefab;
        this.blockMap = blockMap;
        this.sizeX = PositionUtils.getMaxX(blockMap.keySet()) - PositionUtils.getMinX(blockMap.keySet()) + 1;
        this.sizeY = PositionUtils.getMaxY(blockMap.keySet()) - PositionUtils.getMinY(blockMap.keySet()) + 1;
        this.center = Position3D.create(
                sizeX % 2 > 0 ? sizeX / 2 : sizeX / 2 - 1,
                sizeY % 2 > 0 ? sizeY / 2 : sizeY / 2 - 1,
                0
        ).withRelative(offset);
        this.offset = offset;
        this.leftTop = offset.withRelativeX(-MARGIN).withRelativeY(-MARGIN);
        this.rightBot = offset.withRelativeX(sizeX - 1 + MARGIN).withRelativeY(sizeY - 1 + MARGIN);
    }


    public boolean isIntersecting(PrefabData prefabData) {
        return prefabData.rightBot.getX() >= leftTop.getX() &&
                prefabData.rightBot.getY() >= leftTop.getY() &&
                prefabData.leftTop.getX() <= rightBot.getX() &&
                prefabData.leftTop.getY() <= rightBot.getY();
    }


    public Map<Position3D, GameBlock> getBlockMap() {
        Map<Position3D, GameBlock> positionToBlockMap = new HashMap<>();

        for (var positionToBlock : blockMap.entrySet()) {
            positionToBlockMap.put(
                    positionToBlock.getKey().withRelative(offset),
                    positionToBlock.getValue()
            );
        }

        return positionToBlockMap;
    }


    public Position3D getLeftTop(int margin) {
        return leftTop.withRelativeX(MARGIN - margin).withRelativeY(MARGIN - margin);
    }


    public Position3D getRightBot(int margin) {
        return rightBot.withRelativeX(margin - MARGIN).withRelativeY(margin - MARGIN);
    }


    public void removeExtraEntities(Spawner spawner) {
        blockMap.values().stream().filter(GameBlock::hasEntities).forEach(b -> {
            Iterator<AbstractEntity> iterator = b.getEntities().iterator();
            while (iterator.hasNext()) {
                var entity = iterator.next();
                BlockType blockType = TileRepository.getBlockType(entity.getTile());
                if (!spawner.shouldSpawn(blockType)) {
                    iterator.remove();
                    b.updateContent();
                }
            }
        });
    }


    public void shuffleEntities() {
        List<AbstractEntity> entities = new ArrayList<>();

        blockMap.values().stream().filter(GameBlock::hasEntities).forEach(b -> {
            Iterator<AbstractEntity> iterator = b.getEntities().iterator();
            while (iterator.hasNext()) {
                AbstractEntity entity = iterator.next();
                if (entity instanceof Door) continue;

                entities.add(entity);
                iterator.remove();
                b.updateContent();
            }
        });

        if (entities.isEmpty()) return;

        List<Map.Entry<Position3D, GameBlock>> possiblePlaceForSpawn = blockMap.entrySet().stream()
                .filter(e -> {
                    BlockType blockType = TileRepository.getBlockType(e.getValue().getBottomTile());
                    return ENTITY_PLACEMENT_BLOCK_TYPE.contains(blockType);
                })
                .collect(Collectors.toList());
        if (possiblePlaceForSpawn.isEmpty()) return;

        for (AbstractEntity entity : entities) {
            int index = RandomService.getRandom(0, possiblePlaceForSpawn.size() - 1);
            Map.Entry<Position3D, GameBlock> posToBlock = possiblePlaceForSpawn.get(index);
            GameBlock block = posToBlock.getValue();

            entity.setPosition(posToBlock.getKey());
            block.addEntity(entity);
        }
    }
}
