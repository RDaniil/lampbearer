package com.vdn.lampbearer.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hexworks.zircon.api.data.Position;

import java.util.Arrays;
import java.util.List;

public class PositionUtils {

    public static double getDistance(Position from, Position to) {
        return Math.sqrt(
                Math.pow(from.getX() - to.getX(), 2) +
                        Math.pow(from.getY() - to.getY(), 2));
    }


    @Getter
    @AllArgsConstructor
    public enum Direction {
        ALL(Arrays.asList(Octant.values())),
        DOWN(Arrays.asList(Octant.SIXTH, Octant.SEVENTH)),
        LEFT(Arrays.asList(Octant.FOURTH, Octant.FIFTH)),
        RIGHT(Arrays.asList(Octant.FIRST, Octant.EIGHTH)),
        UP(Arrays.asList(Octant.SECOND, Octant.THIRD)),
        UP_LEFT(Arrays.asList(Octant.FIRST, Octant.SECOND)),
        DOWN_LEFT(Arrays.asList(Octant.SEVENTH, Octant.EIGHTH)),
        DOWN_RIGHT(Arrays.asList(Octant.FIFTH, Octant.SIXTH)),
        UP_RIGHT(Arrays.asList(Octant.THIRD, Octant.FOURTH));

        private final List<Octant> octants;
    }

    public enum Octant {
        FIRST,
        SECOND,
        THIRD,
        FOURTH,
        FIFTH,
        SIXTH,
        SEVENTH,
        EIGHTH
    }
}
