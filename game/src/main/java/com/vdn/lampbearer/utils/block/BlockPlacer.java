package com.vdn.lampbearer.utils.block;

import com.vdn.lampbearer.config.LevelProbabilities;
import com.vdn.lampbearer.config.PrefabConfig;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.exception.PlaceTryLimitExceededException;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.prefab.Prefab;
import com.vdn.lampbearer.prefab.PrefabData;
import com.vdn.lampbearer.prefab.PrefabReader;
import com.vdn.lampbearer.services.RandomService;
import com.vdn.lampbearer.services.spawn.Spawner;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;
import org.hexworks.zircon.api.data.Tile;

import java.util.*;

/**
 * @author Chizhov D. on 2024.03.10
 */
public class BlockPlacer {

    /**
     * Количество блоков отступаемых от краев карты от которых будет выбираться место спавна префабов
     */
    private final static Integer PREFAB_SPAWN_MARGIN = 5;
    private final static float[] DEFAULT_PREFAB_SPAWN_RANGE = new float[]{0.2F, 0.8F};


    private final Spawner spawner;
    private final Size3D worldSize;
    private final Map<Position3D, GameBlock> blockMap;

    private final int maxBlockNumber;

    private final Map<Position3D, PrefabData> centerToPrefabDataMap = new HashMap<>();
    private final Set<Position3D> trailEnds = new HashSet<>();


    public BlockPlacer(Spawner spawner, Size3D worldSize, Map<Position3D, GameBlock> blockMap) {
        this.spawner = spawner;
        this.worldSize = worldSize;
        this.blockMap = blockMap;
        this.maxBlockNumber = worldSize.getXLength() * worldSize.getYLength();
    }


    /**
     * @param prefabConfig                    Настройки размещения префаба и сам префаб
     * @throws PlaceTryLimitExceededException предпринято слишком много неудачных попыток
     *                                        разместить prefab
     */
    public void prePlace(PrefabConfig prefabConfig)
            throws PlaceTryLimitExceededException {

        PrefabData prefabData = PrefabReader.read(prefabConfig.getPrefab());

        Map<Position3D, PrefabData> centerToPrefabDataMap = new LinkedHashMap<>();

        int manyPlaceTryNumber = 0;
        while (true) {
            if (++manyPlaceTryNumber > maxBlockNumber)
                throw new PlaceTryLimitExceededException();

            try {
                for (int i = 0; i < prefabConfig.getNumber(); i++) {
                    int onePlaceTryNumber = 0;
                    PrefabData newPrefabData = null;
                    while (newPrefabData == null ||
                            isIntersecting(newPrefabData, centerToPrefabDataMap) ||
                            isIntersecting(newPrefabData, this.centerToPrefabDataMap)) {
                        newPrefabData = prePlace(prefabData, worldSize, prefabConfig);

                        if (++onePlaceTryNumber > maxBlockNumber)
                            throw new PlaceTryLimitExceededException();
                    }

                    if(!prefabConfig.isShouldSpawnEverything()) {
                        newPrefabData.removeExtraEntities(spawner);
                    }
                    if(!prefabConfig.isStaticEntities()){
                        newPrefabData.shuffleEntities();
                    }
                    centerToPrefabDataMap.put(newPrefabData.getCenter(), newPrefabData);
                }
                break;
            } catch (PlaceTryLimitExceededException ignored) {
            }
        }

        this.centerToPrefabDataMap.putAll(centerToPrefabDataMap);

        if (prefabConfig.shouldBeConnectedByTrails()) {
            trailEnds.addAll(centerToPrefabDataMap.keySet());
        }
    }

    /**
     * Пытаемся разместить prefab
     *
     * @param prefabData prefabData
     * @param worldSize  worldSize
     * @return новый вариант размещения prefab'а
     */
    private PrefabData prePlace(PrefabData prefabData, Size3D worldSize,  PrefabConfig prefabConfig) {
        Map<Position3D, GameBlock> blockMap = new HashMap<>();
        if(!isSpecialBuildings(prefabData)) {
            blockMap = BlockFlipper.flip(BlockRotator.rotate(prefabData.getBlockMap()));
        } else {
            if(prefabData.getPrefab().equals(Prefab.LAMPBEARER_BUILDING)) {
                blockMap = BlockFlipper.flip(prefabData.getBlockMap(), BlockFlipper.Direction.LEFT);
            } else if (prefabData.getPrefab().equals(Prefab.LIGHTHOUSE)) {
                blockMap = prefabData.getBlockMap();
            }
        }

        List<Float> xSpawnRange = prefabConfig == null ? null: prefabConfig.getXSpawnRange();
        int[] spawnRangeX = calculateSpawnRange(xSpawnRange, worldSize.getXLength(),
                prefabData.getSizeX());

        List<Float> ySpawnRange = prefabConfig == null ? null: prefabConfig.getYSpawnRange();
        int[] spawnRangeY = calculateSpawnRange(ySpawnRange, worldSize.getYLength(),
                prefabData.getSizeY());

        Position3D offset = Position3D.create(
                RandomService.getRandom(spawnRangeX[0], spawnRangeX[1]),
                RandomService.getRandom(spawnRangeY[0], spawnRangeY[1]),
                0
        );

        return new PrefabData(prefabData.getPrefab(), blockMap, offset);
    }


    private int[] calculateSpawnRange(List<Float> spawnRangeConfig, int worldDimensionLength, int prefabDimensionSize) {
        if (spawnRangeConfig != null && spawnRangeConfig.size() == 2 && spawnRangeConfig.get(0) < spawnRangeConfig.get(1)) {
            int minSpawnPosition = (int) (spawnRangeConfig.get(0) * worldDimensionLength);
            int maxSpawnPosition = (int) (spawnRangeConfig.get(1) * worldDimensionLength);

            minSpawnPosition = Math.max(minSpawnPosition, PREFAB_SPAWN_MARGIN);
            maxSpawnPosition = Math.min(maxSpawnPosition, worldDimensionLength - PREFAB_SPAWN_MARGIN
                    - prefabDimensionSize);

            return new int[] {minSpawnPosition, maxSpawnPosition};
        }
        int minDefault = (int) (worldDimensionLength * DEFAULT_PREFAB_SPAWN_RANGE[0]);
        int maxDefault = (int) (worldDimensionLength * DEFAULT_PREFAB_SPAWN_RANGE[1]);
        return new int[] {minDefault, maxDefault};
    }

    private boolean isSpecialBuildings(PrefabData prefabData) {
        if(prefabData == null || prefabData.getPrefab() == null) {
            return false;
        }
        return prefabData.getPrefab().equals(Prefab.LAMPBEARER_BUILDING) ||
                prefabData.getPrefab().equals(Prefab.LIGHTHOUSE);
    }


    /**
     * @param prefabData            вариант размещения prefab'а
     * @param centerToPrefabDataMap уже размещенные prefab'ы
     * @return true если есть пересечение, иначе false
     */
    private boolean isIntersecting(PrefabData prefabData,
                                   Map<Position3D, PrefabData> centerToPrefabDataMap) {
        return centerToPrefabDataMap.values().stream().anyMatch(d -> d.isIntersecting(prefabData));
    }


    public void place(LevelProbabilities probabilities) {
        Set<Position3D> deadEnds;
        try {
            deadEnds = generateDeadEnds();
        } catch (PlaceTryLimitExceededException e) {
            throw new RuntimeException(e);
        }

        drawTrails(trailEnds, deadEnds, probabilities);

        clearPerimeters(centerToPrefabDataMap.values());

        centerToPrefabDataMap.values().stream().map(PrefabData::getBlockMap)
                .forEach(this::putAllBlocksIntoMap);
    }

    private void putAllBlocksIntoMap(Map<Position3D, GameBlock> prefabBlocks) {
        for (Map.Entry<Position3D, GameBlock> positionToBlock : prefabBlocks.entrySet()) {
            var position = positionToBlock.getKey();
            for (AbstractEntity entity : positionToBlock.getValue().getEntities()) {
                entity.setPosition(position);
            }

        }

        blockMap.putAll(prefabBlocks);
    }


    /**
     * @return тупики
     * @throws PlaceTryLimitExceededException предпринято слишком много неудачных попыток
     *                                        разместить тупик
     */
    private Set<Position3D> generateDeadEnds() throws PlaceTryLimitExceededException {
        int number = (int) (this.trailEnds.size() * .8f);

        Map<Position3D, PrefabData> centerToPrefabDataMap = new LinkedHashMap<>();

        int manyPlaceTryNumber = 0;
        while (true) {
            if (++manyPlaceTryNumber > maxBlockNumber)
                throw new PlaceTryLimitExceededException();

            try {
                for (int i = 0; i < number; i++) {
                    int onePlaceTryNumber = 0;
                    PrefabData newPrefabData = null;
                    while (newPrefabData == null ||
                            isIntersecting(newPrefabData, centerToPrefabDataMap) ||
                            isIntersecting(newPrefabData, this.centerToPrefabDataMap)) {
                        newPrefabData = prePlace(PrefabData.EMPTY, worldSize, null);

                        if (++onePlaceTryNumber > maxBlockNumber)
                            throw new PlaceTryLimitExceededException();
                    }
                    centerToPrefabDataMap.put(newPrefabData.getCenter(), newPrefabData);
                }
                break;
            } catch (PlaceTryLimitExceededException ignored) {
            }
        }

        return centerToPrefabDataMap.keySet();
    }


    /**
     * Рисует тропинки и устанавливает фонари
     *
     * @param ends          точки, в которых обязательно что-то находится
     * @param deadEnds      тупики
     * @param probabilities вероятности
     */
    private void drawTrails(Set<Position3D> ends,
                            Set<Position3D> deadEnds,
                            LevelProbabilities probabilities) {
        List<Position3D> nodes = new ArrayList<>(ends);

        int n = nodes.size();
        int[][] cost = new int[n][n];
        boolean[][] isDrawn = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            Position3D nodeI = nodes.get(i);
            for (int j = i + 1; j < n; j++) {
                cost[i][j] = computeCost(nodeI, nodes.get(j));
                cost[j][i] = cost[i][j];
            }
            cost[i][i] = Integer.MAX_VALUE;
            isDrawn[i][i] = true;
        }

        float[] limit = new float[n];

        for (int i = 0; i < n; i++) {
            Position3D nodeI = nodes.get(i);
            limit[i] = Arrays.stream(cost[i]).min().getAsInt() * 1.2f;

            for (int j = 0; j < ends.size(); j++) {
                if (cost[i][j] <= limit[i] && !isDrawn[i][j]) {
                    ArrayList<Position3D> trail =
                            TrailBuilder.build(nodeI, nodes.get(j), probabilities, worldSize);
                    drawTrail(trail);
                    installLamppost(trail, probabilities);
                    isDrawn[i][j] = isDrawn[j][i] = true;
                }
            }
        }

        List<Position3D> deadNodes = new ArrayList<>(deadEnds);

        cost = new int[deadEnds.size()][ends.size()];

        for (int i = 0; i < deadEnds.size(); i++) {
            Position3D nodeI = deadNodes.get(i);
            for (int j = 0; j < ends.size(); j++) {
                cost[i][j] = computeCost(nodeI, nodes.get(j));
            }
        }

        for (int i = 0; i < deadEnds.size(); i++) {
            Position3D nodeI = deadNodes.get(i);
            float lmt = Arrays.stream(cost[i]).min().getAsInt() * 1.2f;

            for (int j = 0; j < ends.size(); j++) {
                if (cost[i][j] <= lmt) {
                    ArrayList<Position3D> trail =
                            TrailBuilder.build(nodeI, nodes.get(j), probabilities, worldSize);
                    drawTrail(trail);
                    installLamppost(trail, probabilities);
                }
            }
        }
    }


    /**
     * @param from точка А
     * @param to   точка Б
     * @return стоимость (длина) пути из точки А в точку Б
     */
    private int computeCost(Position3D from, Position3D to) {
        return Math.abs(to.getX() - from.getX()) + Math.abs(to.getY() - from.getY());
    }


    /**
     * Очищает периметры, зарезервированные под prefab'ы, от непроходимых объектов
     *
     * @param prefabDataCollection prefab'ы
     */
    private void clearPerimeters(Collection<PrefabData> prefabDataCollection) {
        for (PrefabData prefabData : prefabDataCollection) {
            int margin = 2;
            clearPerimeter(prefabData.getLeftTop(margin), prefabData.getRightBot(margin));
        }
    }


    /**
     * Очищает периметр от непроходимых объектов
     *
     * @param leftTop  левый верхний угол
     * @param rightBot правый нижний угол
     */
    private void clearPerimeter(Position3D leftTop, Position3D rightBot) {
        int maxX = rightBot.getX();
        int maxY = rightBot.getY();
        int minX = leftTop.getX();

        Position3D position = leftTop;
        while (position.getX() <= maxX && position.getY() <= maxY) {

            GameBlock sourceBlock = blockMap.get(position);
            if (sourceBlock != null) {
                Tile bottomTile = sourceBlock.getBottomTile();
                BlockType blockType = TileRepository.getBlockType(bottomTile);
                if (!sourceBlock.isWalkable() || BlockType.isTrail(blockType)) {
                    GameBlock block = GameBlockFactory.returnGameBlock(BlockType.GROUND);
                    block.cloneEntities(sourceBlock.getEntities());
                    blockMap.put(position, block);
                }
            }

            position = position.withRelativeX(1);
            if (position.getX() > maxX) {
                position = position.withX(minX).withRelativeY(1);
            }
        }
    }


    /**
     * @param trail тропинка
     */
    private void drawTrail(ArrayList<Position3D> trail) {
        for (Position3D position3D : trail) {
            blockMap.put(position3D, drawTrail(blockMap.get(position3D)));
        }
    }


    /**
     * @param sourceBlock блок, через который должна пройти тропинка
     * @return новый блок, который должен заменить переданный
     */
    private static GameBlock drawTrail(GameBlock sourceBlock) {
        Tile bottomTile = sourceBlock.getBottomTile();
        BlockType blockType = TileRepository.getBlockType(bottomTile);

        BlockType newBlockType = BlockType.GRASS.equals(blockType) ?
                BlockType.GRASS_TRAIL : BlockType.GROUND_TRAIL;

        GameBlock block = GameBlockFactory.returnGameBlock(newBlockType);
        block.cloneEntities(sourceBlock.getEntities());
        return block;
    }


    /**
     * @param trail         тропинка
     * @param probabilities вероятности
     */
    private void installLamppost(ArrayList<Position3D> trail, LevelProbabilities probabilities) {
        if (trail.isEmpty()) return;

        int random = RandomService.getRandomPercentage();
        if (random > probabilities.getLamppostInstallation()) return;

        Position3D position3D = trail.get(trail.size() / 2);

        GameBlock block = blockMap.get(position3D);
        block.addEntity(AbstractEntity.create(BlockType.LAMPPOST, position3D));
    }
}
