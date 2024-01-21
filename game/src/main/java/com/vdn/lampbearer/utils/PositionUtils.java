package com.vdn.lampbearer.utils;

import lombok.Getter;
import org.hexworks.zircon.api.data.Position;

public class PositionUtils {

    public static double getDistance(Position from, Position to) {
        return Math.sqrt(
                Math.pow(from.getX() - to.getX(), 2) +
                        Math.pow(from.getY() - to.getY(), 2));
    }


    @Getter
    public enum Direction {
        UP_LEFT(Position.create(1, 1)),
        DOWN_LEFT(Position.create(1, -1)),
        DOWN_RIGHT(Position.create(-1, -1)),
        UP_RIGHT(Position.create(-1, 1));

        private final Position position;


        Direction(Position position) {
            this.position = position;
        }
    }
}
