package com.vdn.lampbearer.services.light;

import com.vdn.lampbearer.utils.PositionUtils;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Position3D;

public class CircleLight extends Light {

    public CircleLight(Position position, int radius, TileColor color) {
        super(position, radius, color);
        this.direction = PositionUtils.Direction.ALL;
    }


    public CircleLight(Position3D position, int radius, TileColor color) {
        super(position.to2DPosition(), radius, color);
        this.direction = PositionUtils.Direction.ALL;
    }


    @Override
    public void setDirection(PositionUtils.Direction direction) {
        super.setDirection(PositionUtils.Direction.ALL);
    }
}
