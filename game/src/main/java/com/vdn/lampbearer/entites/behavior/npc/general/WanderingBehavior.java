package com.vdn.lampbearer.entites.behavior.npc.general;

import com.vdn.lampbearer.attributes.PerceptionAttr;
import com.vdn.lampbearer.entites.NonPlayerCharacter;
import com.vdn.lampbearer.entites.behavior.ai.MovementAi;
import com.vdn.lampbearer.game.GameContext;
import org.hexworks.zircon.api.data.Position3D;
import org.jetbrains.annotations.NotNull;

import static com.vdn.lampbearer.services.RandomService.getRandom;

/**
 * A wandering behavior of NPC
 */
public class WanderingBehavior extends NonPlayerCharacterBehavior {
    private final MovementAi movementAi;
    protected Position3D positionToMoveTo;


    public WanderingBehavior(MovementAi ai) {
        this.movementAi = ai;
    }


    @Override
    public boolean act(NonPlayerCharacter npc, GameContext context) {
        if (npc.isStuck(context)) return true;

        while (positionToMoveTo == null || !movementAi.move(npc, positionToMoveTo, context)) {
            positionToMoveTo = getRandomPositionInView(npc);
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
        PerceptionAttr perception = npc.findAttribute(PerceptionAttr.class).get();
        int value = perception.getValue();

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
