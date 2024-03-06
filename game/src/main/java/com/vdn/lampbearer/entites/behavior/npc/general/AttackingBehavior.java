package com.vdn.lampbearer.entites.behavior.npc.general;

import com.vdn.lampbearer.action.reactions.AttackReaction;
import com.vdn.lampbearer.entites.NonPlayerCharacter;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.game.GameContext;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * An attacking behavior of NPC
 */
public class AttackingBehavior extends NonPlayerCharacterBehavior {

    @Override
    public boolean act(NonPlayerCharacter npc, GameContext context) {
        if (npc.isStuck(context)) return true;
        return new AttackReaction().execute(npc, context.getPlayer(), context);
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


    @Override
    public boolean isApplicable(NonPlayerCharacter npc, GameContext context) {
        return npc.getSurroundingPositions().stream()
                .map(context.getWorld()::fetchBlockAtOrNull).filter(Objects::nonNull)
                .anyMatch(b -> b.getEntities().stream().anyMatch(Player.class::isInstance));
    }
}
