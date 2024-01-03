package com.vdn.lampbearer.entites.behavior.npc.general;

import com.vdn.lampbearer.attributes.PerceptionAttr;
import com.vdn.lampbearer.entites.NonPlayerCharacter;
import com.vdn.lampbearer.entites.behavior.ai.MovementAi;
import com.vdn.lampbearer.game.GameContext;
import org.hexworks.zircon.api.data.Position3D;

import java.util.ArrayList;
import java.util.Optional;

/**
 * A chasing behavior of NPC
 */
public class ChasingBehavior extends NonPlayerCharacterBehavior {

    private final MovementAi movementAi;


    public ChasingBehavior(MovementAi ai) {
        this.movementAi = ai;
    }


    @Override
    public boolean act(NonPlayerCharacter npc, GameContext context) {
        return npc.isStuck(context) || movementAi.move(npc, context.getPlayer().getPosition(), context);
    }


    @Override
    public boolean isApplicable(NonPlayerCharacter npc, GameContext context) {
        return isPlayerInSight(npc, context);
    }


    /**
     * Checks if player is in sight
     *
     * @param npc     NPC
     * @param context GameContext
     * @return true if player is in sight
     */
    private boolean isPlayerInSight(NonPlayerCharacter npc, GameContext context) {
        PerceptionAttr perception = npc.findAttribute(PerceptionAttr.class).get();
        int value = perception.getValue();

        Optional<ArrayList<Position3D>> path =
                movementAi.findPath(npc, context.getPlayer().getPosition(), context);
        return path.isPresent() && path.get().size() <= value + 1;
    }
}
