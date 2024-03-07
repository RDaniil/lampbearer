package com.vdn.lampbearer.entites.behavior.npc.general;

import com.vdn.lampbearer.entites.NonPlayerCharacter;
import com.vdn.lampbearer.entites.behavior.ai.movement.MovementAi;
import com.vdn.lampbearer.entites.behavior.ai.olfaction.SmellAi;
import com.vdn.lampbearer.entites.behavior.ai.sight.SightAi;
import com.vdn.lampbearer.game.GameContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.data.Position3D;
import org.jetbrains.annotations.NotNull;

/**
 * A chasing behavior of NPC
 */
@Slf4j
@RequiredArgsConstructor
public class ChasingBehavior extends NonPlayerCharacterBehavior {

    private final SightAi sightAi;
    private final SmellAi smellAi;
    private final MovementAi movementAi;
    private final boolean isMemoryAvailable;

    private Position3D lastSeenPositionOfPlayer;


    @Override
    public boolean act(NonPlayerCharacter npc, GameContext context) {
        if (npc.isStuck(context)) return true;

        Position3D targetPosition;
        if (sightAi.isPlayerInSight(npc, context) || smellAi.isPlayerScentAround(npc, context)) {
            targetPosition = context.getPlayer().getPosition();
        } else {
            targetPosition = lastSeenPositionOfPlayer;
        }

        boolean isMoved = movementAi.move(npc, targetPosition, context);
        if (isMoved && isMemoryAvailable && npc.getPosition().equals(lastSeenPositionOfPlayer)) {
            lastSeenPositionOfPlayer = null;
        }

        return isMoved;
    }


    @NotNull
    @Override
    public NonPlayerCharacterBehavior next(NonPlayerCharacter npc, GameContext context) {
        NonPlayerCharacterBehavior behavior = npc.findBehavior(AttackingBehavior.class);
        if (behavior != null && behavior.isApplicable(npc, context)) return behavior;

        if (isApplicable(npc, context)) return this;

        behavior = npc.findBehavior(WanderingBehavior.class);
        if (behavior != null && behavior.isApplicable(npc, context)) return behavior;

        return this;
    }


    @Override
    public boolean isApplicable(NonPlayerCharacter npc, GameContext context) {
        if (sightAi.isPlayerInSight(npc, context)) {
            if (isMemoryAvailable) {
                lastSeenPositionOfPlayer = context.getPlayer().getPosition();
            }
            return true;
        }

        return smellAi.isPlayerScentAround(npc, context) || lastSeenPositionOfPlayer != null;
    }
}
