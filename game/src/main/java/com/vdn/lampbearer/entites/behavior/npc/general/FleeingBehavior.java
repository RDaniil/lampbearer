package com.vdn.lampbearer.entites.behavior.npc.general;

import com.vdn.lampbearer.attributes.creature.HealthAttr;
import com.vdn.lampbearer.entites.NonPlayerCharacter;
import com.vdn.lampbearer.entites.behavior.ai.movement.MovementAi;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.utils.PositionUtils;
import lombok.RequiredArgsConstructor;
import org.hexworks.zircon.api.data.Position3D;
import org.jetbrains.annotations.NotNull;


/**
 * A fleeing behavior of NPC
 */
@RequiredArgsConstructor
public class FleeingBehavior extends NonPlayerCharacterBehavior {

    private static final int MAXIMUM_FLEEING_DISTANCE = 10;
    private static final float FLEEING_HEALTH_PERCENTAGE = 0.5f;

    private final MovementAi movementAi;
    protected Position3D positionToMoveTo;

    @Override
    public boolean act(NonPlayerCharacter npc, GameContext context) {
        if (npc.isStuck(context)) return true;

        positionToMoveTo = getSafePositionAwayFromThreat(npc, context);
        movementAi.move(npc, positionToMoveTo, context);

        return true;
    }

    @NotNull
    @Override
    public NonPlayerCharacterBehavior next(NonPlayerCharacter npc, GameContext context) {
        if (isApplicable(npc, context)) return this;

        NonPlayerCharacterBehavior behavior = npc.findBehavior(ChasingBehavior.class);
        if (behavior != null && behavior.isApplicable(npc, context)) return behavior;

        behavior = npc.findBehavior(WanderingBehavior.class);
        if (behavior != null && behavior.isApplicable(npc, context)) return behavior;

        return this;
    }

    /**
     * @param npc NPC
     * @return a safe position away from the threat
     */
    private Position3D getSafePositionAwayFromThreat(NonPlayerCharacter npc, GameContext context) {
        Position3D currentPosition = npc.getPosition();
        var threatPosition = context.getPlayer().getPosition();
        int dx = currentPosition.getX() - threatPosition.getX();
        int dy = currentPosition.getY() - threatPosition.getY();

        // Calculate a direction vector that points away from the threat
        int fleeX = dx != 0 ? (dx / Math.abs(dx)) : 0;
        int fleeY = dy != 0 ? (dy / Math.abs(dy)) : 0;

        // Move further away from the threat
        return currentPosition
                .withRelativeX(fleeX)
                .withRelativeY(fleeY);
    }

    @Override
    public boolean isApplicable(NonPlayerCharacter npc, GameContext context) {
        HealthAttr health = npc.findAttribute(HealthAttr.class).get();
        if(health.getMaxHealth() * FLEEING_HEALTH_PERCENTAGE > health.getHealth()){
            return false;
        }


        if(PositionUtils.getDistance(npc.getPosition(), context.getPlayer().getPosition()) > MAXIMUM_FLEEING_DISTANCE){
            return false;
        }

        return true;
    }
}