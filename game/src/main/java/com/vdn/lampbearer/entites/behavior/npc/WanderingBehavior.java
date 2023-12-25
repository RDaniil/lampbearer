package com.vdn.lampbearer.entites.behavior.npc;

import com.vdn.lampbearer.attributes.PerceptionAttr;
import com.vdn.lampbearer.entites.NonPlayerCharacter;
import com.vdn.lampbearer.entites.behavior.ai.Ai;
import com.vdn.lampbearer.game.GameContext;
import org.hexworks.zircon.api.data.Position3D;

import static com.vdn.lampbearer.services.RandomService.getRandom;

/**
 * A wandering behavior of NPC
 */
public class WanderingBehavior extends NonPlayerCharacterBehavior {
    protected Position3D positionToMoveTo;


    public WanderingBehavior(Ai ai) {
        super(ai);
    }


    @Override
    public boolean act(NonPlayerCharacter npc, GameContext context) {
        if (npc.isStuck(context)) return true;

        while (positionToMoveTo == null || !ai.move(npc, positionToMoveTo, context)) {
            positionToMoveTo = getRandomPositionInView(npc);
        }

        return true;
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
