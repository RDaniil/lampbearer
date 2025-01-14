package com.vdn.lampbearer.config;

import com.vdn.lampbearer.exception.PlaceTryLimitExceededException;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.spawn.Spawner;
import com.vdn.lampbearer.utils.block.BlockGenerator;
import com.vdn.lampbearer.utils.block.BlockPlacer;
import com.vdn.lampbearer.views.BlockType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Chizhov D. on 2024.03.14
 */
@Getter
@AllArgsConstructor
public class LevelConfig {

    private final ArrayList<BottomTileConfig> bottomTiles = new ArrayList<>();
    private final LinkedHashMap<BlockType, Integer> stuffRestrictions = new LinkedHashMap<>();
    private final LevelProbabilities probabilities = LevelProbabilities.DEFAULT;
    private final ArrayList<PrefabConfig> prefabs = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    private transient Map<Position3D, GameBlock> positionToBlockMap;


    /**
     * @param worldSize worldSize
     * @return карта уровня
     */
    public Map<Position3D, GameBlock> getMap(Size3D worldSize) {
        if (positionToBlockMap != null) return positionToBlockMap;

        positionToBlockMap = getBottomMap(worldSize);
        Spawner spawner = getSpawner();

        BlockPlacer blockPlacer = new BlockPlacer(spawner, worldSize, positionToBlockMap);

        for (PrefabConfig prefabConfig : prefabs) {
            try {
                blockPlacer.prePlace(prefabConfig);
            } catch (PlaceTryLimitExceededException e) {
                throw new RuntimeException(e);
            }
            blockPlacer.place(probabilities);
        }

        return positionToBlockMap;
    }


    /**
     * @param worldSize worldSize
     * @return "нижний слой" уровня
     */
    private Map<Position3D, GameBlock> getBottomMap(Size3D worldSize) {
        BlockGenerator blockGenerator = new BlockGenerator(worldSize);
        for (BottomTileConfig bottomTile : bottomTiles) {
            blockGenerator.generate(
                    bottomTile.getBlockTypes(),
                    bottomTile.getMinSize(),
                    bottomTile.getMaxSize(),
                    bottomTile.getMinNumber(),
                    bottomTile.getMaxNumber()
            );
        }

        return blockGenerator.getResult();
    }


    /**
     * @return генератор предметов, которые могут быть использованы игроком
     */
    private Spawner getSpawner() {
        Spawner.SpawnerBuilder spawnerBuilder = Spawner.builder();
        for (var blockTypeToMaxNumber : stuffRestrictions.entrySet()) {
            spawnerBuilder.add(blockTypeToMaxNumber.getKey(), blockTypeToMaxNumber.getValue());
        }

        return spawnerBuilder.build();
    }
}
