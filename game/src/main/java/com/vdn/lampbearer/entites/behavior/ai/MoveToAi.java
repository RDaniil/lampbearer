package com.vdn.lampbearer.entites.behavior.ai;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.World;
import org.hexworks.zircon.api.data.Position3D;

public class MoveToAi implements Ai {
    public boolean execute(AbstractEntity entity, Position3D param, GameContext context) {
        //TODO: Почти наверняка возвращать булеан - не достаточно для других поведений
        //TODO: Входные параметры скорее всего надо генерализировать,
        // потмоу что мб нужно больше одного и разных типов

        if (canMoveTo(entity.getPosition(), param, context.getWorld())) {
            context.getWorld().moveEntity(entity, param);
            return true;
        }

        return false;
    }

    private boolean canMoveTo(Position3D position, Position3D param, World world) {
        return true;
    }
}
