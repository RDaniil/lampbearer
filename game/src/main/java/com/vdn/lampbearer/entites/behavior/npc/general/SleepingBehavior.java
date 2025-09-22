package com.vdn.lampbearer.entites.behavior.npc.general;

import com.vdn.lampbearer.entites.NonPlayerCharacter;
import com.vdn.lampbearer.game.GameContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * A wandering behavior of NPC
 */
@RequiredArgsConstructor
@Slf4j
public class SleepingBehavior extends NonPlayerCharacterBehavior {

    @Override
    public boolean act(NonPlayerCharacter npc, GameContext context) {
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


    @Override
    public boolean isApplicable(NonPlayerCharacter npc, GameContext context) {
        return true;
    }
}
