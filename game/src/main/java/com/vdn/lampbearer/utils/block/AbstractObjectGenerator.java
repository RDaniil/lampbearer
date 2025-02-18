package com.vdn.lampbearer.utils.block;

import com.vdn.lampbearer.services.RandomService;
import com.vdn.lampbearer.views.BlockType;
import lombok.AccessLevel;
import lombok.Getter;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;

import java.util.*;

/**
 * @author Chizhov D. on 2024.03.11
 */
@Getter(AccessLevel.PROTECTED)
public abstract class AbstractObjectGenerator {

    private final int maxX;
    private final int maxY;
    private final int maxBlockNumber;
    private final boolean[][] isOccupied;


    public AbstractObjectGenerator(Size3D worldSize) {
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

        Position3D start = getSuitablePosition();
        if (start == null) return;

        Queue<Position3D> queue = new ArrayDeque<>();
        queue.add(start);

        int size = 0;
        while (!queue.isEmpty() && size < targetSize) {
            Position3D position = queue.poll();
            BlockType randomObjectType = getRandomObjectType(blockTypes);
            addGeneratedObject(randomObjectType, position);
            isOccupied[position.getX()][position.getY()] = true;
            size++;

            List<Position3D> unoccupiedNeighbours = getUnoccupiedNeighbours(position);
            if (unoccupiedNeighbours.isEmpty()) continue;

            Collections.shuffle(unoccupiedNeighbours);
            queue.addAll(unoccupiedNeighbours);
        }
    }

    private static BlockType getRandomObjectType(List<BlockType> blockTypes) {
        int randomTypeIndex = RandomService.getRandom(0, blockTypes.size() - 1);
        return blockTypes.get(randomTypeIndex);
    }


    /**
     * @param position позиция
     * @return список соседних незанятых позиций
     */
    protected List<Position3D> getUnoccupiedNeighbours(Position3D position) {
        int x = position.getX();
        int y = position.getY();

        List<Position3D> neighbours = new ArrayList<>();

        if (x > 0 && !isOccupied[x - 1][y]) neighbours.add(position.withRelativeX(-1));
        if (y > 0 && !isOccupied[x][y - 1]) neighbours.add(position.withRelativeY(-1));
        if (x < maxX && !isOccupied[x + 1][y]) neighbours.add(position.withRelativeX(1));
        if (y < maxY && !isOccupied[x][y + 1]) neighbours.add(position.withRelativeY(1));

        return neighbours;
    }


    protected abstract void addGeneratedObject(BlockType objectType, Position3D position);

    protected abstract Position3D getSuitablePosition();
}
