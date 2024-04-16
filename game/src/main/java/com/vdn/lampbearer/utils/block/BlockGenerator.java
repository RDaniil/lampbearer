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
public class BlockGenerator {

    private final int maxX;
    private final int maxY;
    private final boolean[][] isOccupied;
    private final Map<Position3D, GameBlock> blockMap = new HashMap<>();
    private final int maxBlockNumber;


    public BlockGenerator(Size3D worldSize) {
        maxX = worldSize.getXLength() - 1;
        maxY = worldSize.getYLength() - 1;
        isOccupied = new boolean[maxX + 1][maxY + 1];
        maxBlockNumber = worldSize.getXLength() * worldSize.getYLength();
    }


    /**
     * Генерирует группы заданного типа с размером, выбранным случайно
     * из отрезка [minSize, maxSize], в количестве, выбранном случайно
     * из отрезка [minNumber, maxNumber]
     *
     * @param blockTypes типы
     * @param minSize    минимальный размер группы
     * @param maxSize    максимальный размер группы
     * @param minNumber  минимальное количество групп
     * @param maxNumber  максимальное количество групп
     */
    public void generate(List<BlockType> blockTypes,
                         int minSize,
                         int maxSize,
                         int minNumber,
                         int maxNumber) {
        int targetNumber = RandomService.getRandom(minNumber, maxNumber);
        for (int i = 0; i < targetNumber; i++) {
            generate(blockTypes, minSize, maxSize);
        }
    }


    /**
     * Генерирует группу заданного типа с размером, выбранным случайно
     * из отрезка [minSize, maxSize]
     *
     * @param blockTypes типы
     * @param minSize    минимальный размер группы
     * @param maxSize    максимальный размер группы
     */
    private void generate(List<BlockType> blockTypes, int minSize, int maxSize) {
        int targetSize = RandomService.getRandom(minSize, maxSize);

        Position3D start = getAnyUnusedPosition();
        if (start == null) return;

        Queue<Position3D> queue = new ArrayDeque<>();
        queue.add(start);

        int size = 0;
        while (!queue.isEmpty() && size < targetSize) {
            Position3D position = queue.poll();
            int randomTypeIndex = RandomService.getRandom(0, blockTypes.size() - 1);
            GameBlock block = GameBlockFactory.returnGameBlock(blockTypes.get(randomTypeIndex));
            blockMap.put(position, block);
            isOccupied[position.getX()][position.getY()] = true;
            size++;

            List<Position3D> unoccupiedNeighbours = getUnoccupiedNeighbours(position);
            if (unoccupiedNeighbours.isEmpty()) continue;

            Collections.shuffle(unoccupiedNeighbours);
            queue.addAll(unoccupiedNeighbours);
        }
    }


    /**
     * @param position позиция
     * @return список соседних незанятых позиций
     */
    private List<Position3D> getUnoccupiedNeighbours(Position3D position) {
        int x = position.getX();
        int y = position.getY();

        List<Position3D> neighbours = new ArrayList<>();

        if (x > 0 && !isOccupied[x - 1][y]) neighbours.add(position.withRelativeX(-1));
        if (y > 0 && !isOccupied[x][y - 1]) neighbours.add(position.withRelativeY(-1));
        if (x < maxX && !isOccupied[x + 1][y]) neighbours.add(position.withRelativeX(1));
        if (y < maxY && !isOccupied[x][y + 1]) neighbours.add(position.withRelativeY(1));

        return neighbours;
    }


    /**
     * @return позицию, если существует, иначе null
     */
    private Position3D getAnyUnusedPosition() {
        Position3D start = null;

        try {
            int tryNumber = 0;
            while (start == null || isOccupied[start.getX()][start.getY()]) {
                start = Position3D.create(
                        RandomService.getRandom(0, maxX),
                        RandomService.getRandom(0, maxY),
                        0
                );

                if (++tryNumber > maxBlockNumber) throw new PlaceTryLimitExceededException();
            }
        } catch (PlaceTryLimitExceededException ignored) {
            if (isWorldFull()) return null;

            for (int i = 0; i <= maxX; i++) {
                for (int j = 0; j <= maxY; j++) {
                    if (isOccupied[i][j]) continue;

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

        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                if (isOccupied[i][j]) continue;

                blockMap.put(
                        Position3D.create(i, j, 0),
                        GameBlockFactory.returnGameBlock(BlockType.GROUND)
                );
                isOccupied[i][j] = true;
            }
        }
        return blockMap;
    }


    private boolean isWorldFull() {
        return blockMap.size() >= maxBlockNumber;
    }
}
