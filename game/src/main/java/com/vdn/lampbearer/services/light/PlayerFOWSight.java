package com.vdn.lampbearer.services.light;

import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;

/**
 * "Свет" вокруг игрока, рассеивающий туман войны, когда у игрока нет других источников света
 */
public class PlayerFOWSight extends CircleLight {

    public void setPosition(Position pos) {
        super.setPosition(pos);
    }


    public PlayerFOWSight(Position position, int radius, TileColor color) {
        super(position, radius, color);
    }
}
