package com.vdn.lampbearer.action;

import com.vdn.lampbearer.entites.item.interfaces.Projectile;
import com.vdn.lampbearer.game.GameContext;
import org.hexworks.zircon.api.data.Position3D;

/**
 * Реакция, в процессе которой в мире появляется снаряд
 */
public abstract class AbstractProjectileReaction implements TargetedReaction {

    protected void initProjectile(Projectile projectile, Position3D targetPosition,
                                  Position3D initiatorPosition, GameContext context) {
        projectile.setTargetPosition(targetPosition);
        projectile.setPosition(initiatorPosition);
        projectile.setFlying(true);

        projectile.beforeLaunch(context, initiatorPosition.to2DPosition(), targetPosition.to2DPosition());

        context.getWorld().addEntity(projectile, initiatorPosition);
    }
}
