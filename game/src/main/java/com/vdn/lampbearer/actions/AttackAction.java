package com.vdn.lampbearer.actions;

import com.vdn.lampbearer.attributes.HealthAttr;
import com.vdn.lampbearer.attributes.StrengthAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class AttackAction implements Action {
    public void execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        log.info("Attacking");
        Optional<StrengthAttr> strengthAttr = initiator.findAttribute(StrengthAttr.class);
        if (strengthAttr.isEmpty()) return;

        HealthAttr healthAttr = target.findAttribute(HealthAttr.class).get();
        int remainingHealth = healthAttr.reduceHealth(strengthAttr.get().getValue());
        if (remainingHealth <= 0) {
            context.getWorld().removeEntity(target, target.getPosition());
        }
    }
}
