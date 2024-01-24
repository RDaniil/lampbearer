package com.vdn.lampbearer.services.light;

import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Position3D;

public class CircleLight extends Light {
    public CircleLight(Position position, int radius, TileColor color) {
        super(position, radius, color);
    }


    public CircleLight(Position3D position, int radius, TileColor color) {
        super(position.to2DPosition(), radius, color);
    }


    public CircleLight(int radius, TileColor color) {
        super(radius, color);
    }
}
