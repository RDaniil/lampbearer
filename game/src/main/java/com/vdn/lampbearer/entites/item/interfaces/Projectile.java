package com.vdn.lampbearer.entites.item.interfaces;

import com.vdn.lampbearer.action.reactions.ProjectileAttackReaction;
import com.vdn.lampbearer.attributes.occupation.BlockOccupier;
import com.vdn.lampbearer.attributes.projectile.PenetrationPowerAttr;
import com.vdn.lampbearer.attributes.projectile.ProjectileDamageAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.interfaces.Updatable;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.game.GameContext;
import lombok.Getter;
import lombok.Setter;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.shape.LineFactory;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Optional;

@Setter
@Getter
public abstract class Projectile extends AbstractItem implements Updatable {

    public PenetrationPowerAttr penetrationPower;
    public ProjectileDamageAttr projectileDamage;
    public Position3D targetPosition;

    @Nullable
    protected Iterator<Position> projectilePath = null;
    protected boolean isFlying;


    public Projectile(Position3D position) {
        super(position);
    }


    @Override
    public int getTime() {
        return 0;
    }


    @Override
    public boolean needUpdate() {
        return isFlying;
    }


    @Override
    public void update(GameContext context) {
        if (!needUpdate()) {
            return;
        }
        updateProjectile(context);
    }


    /**
     * Обновляет состояние снаряда. Передвигает его по миру, проверяет на коллизии
     * с сущностями и блоками, уничтожает при необходимости.
     * При столкновении с сущностями наносит урон.
     *
     * @param context игровой контекст
     */
    protected void updateProjectile(GameContext context) {
        if (projectilePath == null) {
            initPath();
        }

        var nextPosition = projectilePath.next();
        moveProjectile(context, nextPosition);
        if (isCollided(context)) {
            onCollision(context);
        }

        if (!projectilePath.hasNext()) {
            onPathEnd(context);
        }
    }


    private void moveProjectile(GameContext context, Position nextPosition) {
        context.getWorld().moveEntity(this, nextPosition.toPosition3D(0));
    }


    protected void initPath() {
        projectilePath = LineFactory.INSTANCE.buildLine(getPosition().to2DPosition(),
                getTargetPosition().to2DPosition()).iterator();
        //Сдвигаем позицию один раз, чтобы не спавнить снаряд на стреляющем
        projectilePath.next();
    }


    protected boolean isCollided(GameContext context) {
        return !context.getWorld().isBlockWalkable(position);
    }


    protected void onPathEnd(GameContext context) {
        isFlying = false;
        projectilePath = null;
        context.getWorld().removeFromSchedule(this);
    }


    protected void onCollision(GameContext context) {
        var attackableEntityAtPosition = getAttackableEntityAtPosition(context, position.to2DPosition());
        if (attackableEntityAtPosition.isPresent()) {
            attackEntityWithProjectile(context, attackableEntityAtPosition.get());
        } else {
            destroyProjectile(context);
        }
    }


    protected void attackEntityWithProjectile(GameContext context,
                                              AbstractEntity entityAtCurrentPosition) {
        boolean executed = new ProjectileAttackReaction().execute(
                this, entityAtCurrentPosition, context);
        if (executed) {
            if (getPenetrationPower().useOnce() == 0) {
                destroyProjectile(context);
            }
        }
    }


    protected void destroyProjectile(GameContext context) {
        isFlying = false;
        context.getWorld().deleteEntity(this, getPosition());
    }


    private static Optional<AbstractEntity> getAttackableEntityAtPosition(GameContext context, Position nextPosition) {
        return context.getWorld()
                .getEntitiesAt(nextPosition).stream()
                .filter(e -> e.hasAttribute(BlockOccupier.class))
                .findFirst();
    }
}
