package com.vdn.lampbearer.action.reactions;

import com.vdn.lampbearer.action.AbstractProjectileReaction;
import com.vdn.lampbearer.attributes.InventoryAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.entites.item.firearm.AbstractFirearm;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.data.Position3D;

import java.util.Optional;

@Slf4j
public class ShootFirearmReaction extends AbstractProjectileReaction {

    /*TODO: упущение данной системы - если сущность хочет стрелять из оружия
     *  мы должны иметь доступ и к сущности и к конкретному оружию внутри этого метода.
     * Для того, чтобы использовать атрибуты. (Например на основе какого-то атрибута высчитывать
     * шанс попадания.
     * Сейчас здесь оружие берется из сущности, но если у сущности будет несколько видов оружия -
     * не понятно что делать */
    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        Position3D initiatorPosition = initiator.getPosition();
        Position3D targetPosition = target.getPosition();

        Optional<InventoryAttr> inventoryOpt = initiator.findAttribute(InventoryAttr.class);
        if (inventoryOpt.isEmpty()) return false;

        Optional<AbstractItem> foundFirearm = inventoryOpt.get().findItem(AbstractFirearm.class);
        if (foundFirearm.isEmpty()) {
            return false;
        }

        if (foundFirearm.get() instanceof AbstractFirearm) {
            var firearm = (AbstractFirearm<?>) foundFirearm.get();
            var projectile = firearm.popProjectile();
            if (projectile == null) {
                context.getLogArea().addParagraph("*You hear a click, but gun doesn't shoot*",
                        false, 0);
                return true;
            }
            initProjectile(projectile, targetPosition, initiatorPosition, context);
            inventoryOpt.get().removeItem(projectile);
        }

        return true;
    }
}
