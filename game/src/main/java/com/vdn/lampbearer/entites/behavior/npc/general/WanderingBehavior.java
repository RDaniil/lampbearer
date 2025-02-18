package com.vdn.lampbearer.entites.behavior.npc.general;

import com.vdn.lampbearer.attributes.creature.PerceptionAttr;
import com.vdn.lampbearer.entites.NonPlayerCharacter;
import com.vdn.lampbearer.entites.behavior.ai.movement.MovementAi;
import com.vdn.lampbearer.game.GameContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.data.Position3D;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.vdn.lampbearer.services.RandomService.getRandom;

/**
 * A wandering behavior of NPC
 */
@RequiredArgsConstructor
@Slf4j
public class WanderingBehavior extends NonPlayerCharacterBehavior {

    private final static int MAX_TRIES_UNTIL_SWITCHING_TO_WAITING = 5;

    private final MovementAi movementAi;
    private boolean isSwitchingToWating = false;
    protected Position3D positionToMoveTo;


    @Override
    public boolean act(NonPlayerCharacter npc, GameContext context) {
        if (npc.isStuck(context)) return true;
        int numberOfTries = 0;
        while (positionToMoveTo == null || !movementAi.move(npc, positionToMoveTo, context)) {
            positionToMoveTo = getRandomPositionInView(npc);
            numberOfTries++;
            if(numberOfTries > MAX_TRIES_UNTIL_SWITCHING_TO_WAITING){
                log.info(String.format("Entity %s is waiting", npc));
                return true;
            }
        }

        return true;
    }


    @NotNull
    @Override
    public NonPlayerCharacterBehavior next(NonPlayerCharacter npc, GameContext context) {
        NonPlayerCharacterBehavior behavior = npc.findBehavior(AttackingBehavior.class);
        if (behavior != null && behavior.isApplicable(npc, context)) return behavior;

        behavior = npc.findBehavior(ChasingBehavior.class);
        if (behavior != null && behavior.isApplicable(npc, context)) return behavior;

        return this;
    }


    /**
     * @param npc NPC
     * @return a target position to wander to
     */
    private Position3D getRandomPositionInView(NonPlayerCharacter npc) {
        int value = 2;

        int randomX = getRandom(1, value);
        int randomY = getRandom(1, value);

        return npc.getPosition()
                .withRelativeX(randomX % 2 == 0 ? randomX : -randomX)
                .withRelativeY(randomY % 2 == 0 ? randomY : -randomY);
    }


    @Override
    public boolean isApplicable(NonPlayerCharacter npc, GameContext context) {
        return true;
    }
}
