package com.vdn.lampbearer.entites.behavior.ai;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.shape.LineFactory;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Bresenham's line algorithm
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LinearAi extends Ai {

    private static LinearAi INSTANCE;


    public static LinearAi getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new LinearAi());
    }


    @Override
    protected ArrayList<Position3D> createPath(Position3D startPos, Position3D endPos) {
        return LineFactory.INSTANCE
                .buildLine(startPos.to2DPosition(), endPos.to2DPosition())
                .getPositions().stream()
                .map(p -> p.toPosition3D(0))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
