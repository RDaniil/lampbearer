package com.vdn.lampbearer.action.reactions.combat;

import com.vdn.lampbearer.action.reactions.Reaction;
import com.vdn.lampbearer.attributes.creature.HealthAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.entites.item.interfaces.Projectile;
import com.vdn.lampbearer.game.GameContext;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.builder.graphics.CharacterTileStringBuilder;
import org.hexworks.zircon.api.component.LogArea;
import org.hexworks.zircon.api.data.Tile;

import java.util.Optional;

public class ProjectileAttackReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        if (!(initiator instanceof Projectile)) {
            return false;
        }
        var projectile = (Projectile) initiator;

        Optional<HealthAttr> healthAttr = target.findAttribute(HealthAttr.class);
        if (healthAttr.isEmpty()) {
            return true;
        }
        int projectileDamage = projectile.getProjectileDamage().getValue();
        int remainingHealth = healthAttr.get().reduceHealth(projectileDamage);
        context.getScoreService().addDamageDealt(projectileDamage);


        logEnemy(context.getLogArea(), initiator.getName(),  initiator.getTile());
        context.getLogArea().addInlineText(" attacked ");
        logEnemy(context.getLogArea(), target.getName(), target.getTile());
        context.getLogArea().addInlineText(String.format(
                " for %d damage",
                projectileDamage
        ));
        context.getLogArea().commitInlineElements();

        if (remainingHealth <= 0) {
            logEnemy(context.getLogArea(), target.getName(), target.getTile());
            context.getLogArea().addInlineText(" died!");
            context.getWorld().deleteEntity(target, target.getPosition());

            if (target instanceof Player) {
                context.getLogArea().addParagraph("GAME IS OVER", false, 0);
                throw new RuntimeException("GAME IS OVER");
            }
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
