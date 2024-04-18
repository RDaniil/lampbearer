package com.vdn.lampbearer.entites.item.projectile.revolver;

import com.vdn.lampbearer.entites.item.interfaces.Projectile;
import org.hexworks.zircon.api.data.Position3D;

public abstract class AbstractRevolverRound extends Projectile {
    public AbstractRevolverRound(Position3D position) {
        super(position);
    }
}
