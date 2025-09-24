package com.vdn.lampbearer.action.reactions.combat;

import com.vdn.lampbearer.action.actions.combat.AttackAction;
import com.vdn.lampbearer.action.reactions.Reaction;
import com.vdn.lampbearer.attributes.creature.HealthAttr;
import com.vdn.lampbearer.attributes.creature.StrengthAttr;
import com.vdn.lampbearer.constants.GameOverResult;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.exception.GameOverException;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class AttackReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        if (target.findAction(AttackAction.class).isEmpty()) return false;

        Optional<StrengthAttr> strengthAttr = initiator.findAttribute(StrengthAttr.class);
        if (strengthAttr.isEmpty()) return false;

        HealthAttr healthAttr = target.findAttribute(HealthAttr.class).get();
        int damageAmount = strengthAttr.get().getValue();
        int remainingHealth = healthAttr.reduceHealth(damageAmount);
        if (target instanceof Player) {
            context.getScoreService().addDamageReceived(damageAmount);
        } else if (initiator instanceof Player) {
            context.getScoreService().addDamageDealt(damageAmount);
        }
        log.info(String.format("%s's attacked %s for %d damage",
                initiator.getName(),
                target.getName(),
                damageAmount
        ));
        context.getLogArea().addInlineComponent(initiator.getName(), initiator.getTile().getForegroundColor());
        context.getLogArea().addInlineText(" attacked ");
        context.getLogArea().addInlineText(String.format(
                " for %d damage",
                damageAmount
        ));
        context.getLogArea().commitInlineElements();

        if (remainingHealth <= 0) {
            context.getLogArea().addInlineComponent(target.getName(), target.getTile().getForegroundColor());
            context.getLogArea().addInlineText(" died!");
            context.getLogArea().commitInlineElements();
            context.getWorld().deleteEntity(target, target.getPosition());

            if (target instanceof Player) {
                context.getLogArea().addParagraph("GAME IS OVER");
                throw new GameOverException(context, GameOverResult.PLAYER_DEAD);
            }

            context.getScoreService().addKilledEnemy(target);
        }

        return true;
    }
}
