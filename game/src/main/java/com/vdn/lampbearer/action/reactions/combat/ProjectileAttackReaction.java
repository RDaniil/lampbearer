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


        context.getLogArea().addInlineComponent(initiator.getName(), initiator.getTile().getForegroundColor());
        context.getLogArea().addInlineText(" attacked ");
        context.getLogArea().addInlineComponent(target.getName(), target.getTile().getForegroundColor());
        context.getLogArea().addInlineText(String.format(
                " for %d damage",
                projectileDamage
        ));
        context.getLogArea().commitInlineElements();

        if (remainingHealth <= 0) {
            context.getLogArea().addInlineComponent(target.getName(), target.getTile().getForegroundColor());
            context.getLogArea().addInlineText(" died!");
            context.getLogArea().commitInlineElements();
            context.getWorld().deleteEntity(target, target.getPosition());

            if (target instanceof Player) {
                context.getLogArea().addParagraph("GAME IS OVER", false, 0);
                throw new RuntimeException("GAME IS OVER");
            }
        }

        return true;
    }
}
