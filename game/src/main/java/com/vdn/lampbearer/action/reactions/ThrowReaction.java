package com.vdn.lampbearer.action.reactions;

import com.vdn.lampbearer.action.TargetedReaction;
import com.vdn.lampbearer.action.actions.ThrowAction;
import com.vdn.lampbearer.attributes.InventoryAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.entites.item.interfaces.Projectile;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.data.Position3D;

import java.util.List;
import java.util.Optional;

@Slf4j
public class ThrowReaction extends TargetedReaction {
    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        Position3D initiatorPosition = initiator.getPosition();
        Position3D targetPosition = target.getPosition();

        Optional<InventoryAttr> inventoryOpt = initiator.findAttribute(InventoryAttr.class);
        if (inventoryOpt.isEmpty()) return false;

        List<AbstractItem> throwables = inventoryOpt.get().findItemByAction(ThrowAction.class);
        if (throwables.isEmpty()) {
            return false;
        }

        if (throwables.get(0) instanceof Projectile) {
            Projectile projectile = (Projectile) throwables.get(0);
            initProjectile(projectile, targetPosition, initiatorPosition, context);
            inventoryOpt.get().removeItem(projectile);
        }

        return true;
    }


    private static void initProjectile(Projectile projectile, Position3D targetPosition,
                                       Position3D initiatorPosition, GameContext context) {
        projectile.setTargetPosition(targetPosition);
        projectile.setPosition(initiatorPosition);
        projectile.setFlying(true);

        context.getWorld().addEntity(projectile, initiatorPosition);
    }
}
