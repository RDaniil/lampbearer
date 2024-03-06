package com.vdn.lampbearer.entites.behavior.npc.general;

import com.vdn.lampbearer.entites.NonPlayerCharacter;
import com.vdn.lampbearer.entites.behavior.Behavior;
import com.vdn.lampbearer.game.GameContext;
import org.jetbrains.annotations.NotNull;

/**
 * A behavior of NPC
 */
public abstract class NonPlayerCharacterBehavior extends Behavior<NonPlayerCharacter> {

    @NotNull
    @Override
    public abstract NonPlayerCharacterBehavior next(NonPlayerCharacter npc, GameContext context);

    /**
     * Checks if behavior is applicable
     *
     * @param npc     NPC
     * @param context GameContext
     * @return true if it's applicable
     */
    public abstract boolean isApplicable(NonPlayerCharacter npc, GameContext context);
}
