package com.vdn.lampbearer.action.reactions;

import com.vdn.lampbearer.action.actions.items.ThrowAction;
import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.entites.item.interfaces.Projectile;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.data.Position3D;

import java.util.List;
import java.util.Optional;

@Slf4j
public class ThrowReaction extends AbstractProjectileReaction {
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

}
