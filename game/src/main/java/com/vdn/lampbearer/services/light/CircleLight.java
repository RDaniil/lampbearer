package com.vdn.lampbearer.services.light;

import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;

public class CircleLight extends Light {
    public CircleLight(Position position, int radius, TileColor color) {
        super(position, radius, color);
    }
}
