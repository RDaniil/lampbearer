package com.vdn.lampbearer.entites.behavior.npc;

import com.vdn.lampbearer.entites.NonPlayerCharacter;
import com.vdn.lampbearer.entites.behavior.Behavior;
import com.vdn.lampbearer.entites.behavior.ai.Ai;
import com.vdn.lampbearer.game.GameContext;

/**
 * A behavior of NPC
 */
public abstract class NonPlayerCharacterBehavior extends Behavior<NonPlayerCharacter> {

    protected final Ai ai;


    public NonPlayerCharacterBehavior(Ai ai) {
        this.ai = ai;
    }


    /**
     * Checks if behavior is applicable
     *
     * @param npc     NPC
     * @param context GameContext
     * @return true if it's applicable
     */
    public abstract boolean isApplicable(NonPlayerCharacter npc, GameContext context);
}
