package com.vdn.lampbearer.services.spawn;

import com.vdn.lampbearer.services.RandomService;
import com.vdn.lampbearer.views.BlockType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Chizhov D. on 2024.03.12
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Spawner {

    private final Map<BlockType, SpawnStats> typeToStatsMap = new HashMap<>();


    public boolean shouldSpawn(BlockType blockType) {
        SpawnStats spawnStats = typeToStatsMap.get(blockType);
        if (spawnStats == null) return true;
        if (spawnStats.isEnough()) return false;

        int random = RandomService.getRandom(1, 100);
        if (random < spawnStats.getSpawnedPercentage()) return false;

        spawnStats.inc();
        return true;
    }


    public static SpawnerBuilder builder() {
        return new SpawnerBuilder();
    }


    public static class SpawnerBuilder {

        private final Map<BlockType, Integer> typeToMaxNumberMap = new HashMap<>();


        public SpawnerBuilder add(BlockType blockType, int maxNumber) {
            typeToMaxNumberMap.put(blockType, maxNumber);
            return this;
        }


        public Spawner build() {
            Spawner spawner = new Spawner();
            typeToMaxNumberMap.forEach((k, v) -> spawner.typeToStatsMap.put(k, new SpawnStats(v)));
            return spawner;
        }
    }

    private static class SpawnStats {

        private final int maxNumber;
        private int spawnedNumber;


        private SpawnStats(int maxNumber) {
            if (maxNumber < 0) {
                String s = "Количество объектов для генерации должно быть неотрицательным!";
                throw new RuntimeException(s);
            }

            this.maxNumber = maxNumber;
        }


        private void inc() {
            spawnedNumber++;
        }


        private float getSpawnedPercentage() {
            return (float) spawnedNumber / maxNumber * 100;
        }


        private boolean isEnough() {
            return spawnedNumber >= maxNumber;
        }
    }
}
