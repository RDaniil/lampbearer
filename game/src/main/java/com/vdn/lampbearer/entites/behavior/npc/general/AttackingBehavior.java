package com.vdn.lampbearer.entites.behavior.npc.general;

import com.vdn.lampbearer.entites.NonPlayerCharacter;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.reactions.AttackingReaction;

import java.util.Objects;

/**
 * An attacking behavior of NPC
 */
public class AttackingBehavior extends NonPlayerCharacterBehavior {

    @Override
    public boolean act(NonPlayerCharacter npc, GameContext context) {
        if (npc.isStuck(context)) return true;
        return new AttackingReaction().execute(npc, context.getPlayer(), context);
    }


    @Override
    public boolean isApplicable(NonPlayerCharacter npc, GameContext context) {
        return npc.getSurroundingPositions().stream()
                .map(context.getWorld()::fetchBlockAtOrNull).filter(Objects::nonNull)
                .anyMatch(b -> b.getEntities().stream().anyMatch(Player.class::isInstance));
    }
}
