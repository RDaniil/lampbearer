package com.vdn.lampbearer.entites.behavior.npc;

import com.vdn.lampbearer.entites.NonPlayerCharacter;
import com.vdn.lampbearer.entites.behavior.ai.LinearMovementAi;
import com.vdn.lampbearer.entites.behavior.npc.general.AttackingBehavior;
import com.vdn.lampbearer.entites.behavior.npc.general.ChasingBehavior;
import com.vdn.lampbearer.entites.behavior.npc.general.NonPlayerCharacterBehavior;
import com.vdn.lampbearer.entites.behavior.npc.general.WanderingBehavior;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SimpleZombieBehavior extends NonPlayerCharacterBehavior {

    private final List<NonPlayerCharacterBehavior> behaviors = new ArrayList<>();


    public SimpleZombieBehavior() {
        behaviors.add(new AttackingBehavior());
        behaviors.add(new ChasingBehavior(LinearMovementAi.getInstance()));
        behaviors.add(new WanderingBehavior(LinearMovementAi.getInstance()));
    }


    @Override
    public boolean act(NonPlayerCharacter actor, GameContext context) {
        for (NonPlayerCharacterBehavior behavior : behaviors) {
            if (behavior.isApplicable(actor, context)) {
                log.info(String.format("%s using %s", actor.getName(),
                        behavior.getClass().getCanonicalName()));
                return behavior.act(actor, context);
            }
        }
        return false;
    }


    @Override
    public boolean isApplicable(NonPlayerCharacter npc, GameContext context) {
        return true;
    }
}
