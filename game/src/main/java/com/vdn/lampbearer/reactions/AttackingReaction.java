package com.vdn.lampbearer.reactions;

import com.vdn.lampbearer.action.AttackAction;
import com.vdn.lampbearer.attributes.HealthAttr;
import com.vdn.lampbearer.attributes.StrengthAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class AttackingReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        if (target.findAction(AttackAction.class).isEmpty()) return false;

        Optional<StrengthAttr> strengthAttr = initiator.findAttribute(StrengthAttr.class);
        if (strengthAttr.isEmpty()) return false;

        HealthAttr healthAttr = target.findAttribute(HealthAttr.class).get();
        int remainingHealth = healthAttr.reduceHealth(strengthAttr.get().getValue());
        log.info(String.format("%s's attacked %s for %d damage",
                initiator.getName(),
                target.getName(),
                strengthAttr.get().getValue()
        ));

        context.getLogArea().addParagraph(
                String.format(
                        "%s's attacked %s for %d damage",
                        initiator.getName(),
                        target.getName(),
                        strengthAttr.get().getValue()
                ),
                false,
                10
        );

        if (remainingHealth <= 0) {
            context.getLogArea()
                    .addParagraph(String.format("%s's died!", target.getName()), false, 10);
            context.getWorld().removeEntity(target, target.getPosition());

            if (target instanceof Player) {
                context.getLogArea().addParagraph("GAME IS OVER", false, 10);
                throw new RuntimeException("GAME IS OVER");
            }
        }

        return true;
    }
}
