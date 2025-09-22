package com.vdn.lampbearer.entites.item.projectile.revolver;

import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Tile;

public class EmptyRound extends AbstractRevolverRound {

    public EmptyRound(Position3D position) {
        super(position);
    }

    @Override
    public Tile getTile() {
        return Tile.empty();
    }
}
