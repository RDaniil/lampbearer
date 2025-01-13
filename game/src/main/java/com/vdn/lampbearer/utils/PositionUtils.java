package com.vdn.lampbearer.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Position3D;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class PositionUtils {

    public static double getDistance(Position from, Position to) {
        return Math.sqrt(
                Math.pow(from.getX() - to.getX(), 2) +
                        Math.pow(from.getY() - to.getY(), 2));
    }

    public static double getDistance(Position3D from, Position3D to) {
        return getDistance(from.to2DPosition(), to.to2DPosition());
    }


    @SuppressWarnings({"all"})
    public static int getMaxX(Set<Position3D> positions) {
        return positions.stream().mapToInt(Position3D::getX).max().getAsInt();
    }


    @SuppressWarnings({"all"})
    public static int getMaxY(Set<Position3D> positions) {
        return positions.stream().mapToInt(Position3D::getY).max().getAsInt();
    }


    @SuppressWarnings({"all"})
    public static int getMinX(Set<Position3D> positions) {
        return positions.stream().mapToInt(Position3D::getX).min().getAsInt();
    }


    @SuppressWarnings({"all"})
    public static int getMinY(Set<Position3D> positions) {
        return positions.stream().mapToInt(Position3D::getY).min().getAsInt();
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
