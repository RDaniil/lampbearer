package com.vdn.lampbearer.services.light;

import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position3D;

/**
 * @author Chizhov D. on 2024.03.03
 */
public class DirectionalLight extends Light {

    public DirectionalLight(Position3D position, int radius, TileColor color) {
        super(position.to2DPosition(), radius, color);
    }
}
