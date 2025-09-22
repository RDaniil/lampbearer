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
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.builder.graphics.CharacterTileStringBuilder;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.component.LogArea;
import org.hexworks.zircon.api.data.Tile;

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
        logEnemy(context.getLogArea(), initiator.getName(),  initiator.getTile());
        context.getLogArea().addInlineText(" attacked ");
        logEnemy(context.getLogArea(), target.getName(), target.getTile());
        context.getLogArea().addInlineText(String.format(
                " for %d damage",
                damageAmount
        ));
        context.getLogArea().commitInlineElements();

        if (remainingHealth <= 0) {
            logEnemy(context.getLogArea(), target.getName(), target.getTile());
            context.getLogArea().addInlineText(" died!");
            context.getWorld().deleteEntity(target, target.getPosition());
            context.getLogArea().commitInlineElements();

            if (target instanceof Player) {
                context.getLogArea().addParagraph("GAME IS OVER", false, 0);
                throw new GameOverException(context, GameOverResult.PLAYER_DEAD);
            }

            context.getScoreService().addKilledEnemy(target);
        }

        return true;
    }

    private void logEnemy(LogArea logArea, String name, Tile tile) {
        logArea
                .addInlineComponent(
                        Components.label()
                                .withText(name)
                                .withComponentRenderer((graphics, cntxt) -> {
                                    var bang = CharacterTileStringBuilder.newBuilder()
                                            .withText(name)
                                            .withForegroundColor(tile.getForegroundColor())
                                            .build();
                                    graphics.draw(bang);
                                }).build());

    }
}
