package com.vdn.lampbearer.action.reactions.combat;

import com.vdn.lampbearer.action.reactions.Reaction;
import com.vdn.lampbearer.attributes.creature.HealthAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.entites.item.interfaces.Projectile;
import com.vdn.lampbearer.game.GameContext;

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
        int remainingHealth = healthAttr.get().reduceHealth(projectile.getProjectileDamage().getValue());

        context.getLogArea().addParagraph(
                String.format(
                        "%s's been attacked by %s for %d damage",
                        target.getName(),
                        initiator.getName(),
                        projectile.getProjectileDamage().getValue()
                ),
                false,
                0
        );

        if (remainingHealth <= 0) {
            context.getLogArea()
                    .addParagraph(String.format("%s's died!", target.getName()), false, 0);
            context.getWorld().deleteEntity(target, target.getPosition());

            if (target instanceof Player) {
                context.getLogArea().addParagraph("GAME IS OVER", false, 0);
                throw new RuntimeException("GAME IS OVER");
            }
        }

        return true;
    }
}
