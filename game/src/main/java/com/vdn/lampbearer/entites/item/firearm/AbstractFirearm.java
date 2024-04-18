package com.vdn.lampbearer.entites.item.firearm;

import com.vdn.lampbearer.attributes.inventory.ItemContainerAttr;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.entites.item.interfaces.Projectile;
import org.hexworks.zircon.api.data.Position3D;

public abstract class AbstractFirearm<P extends Projectile> extends AbstractItem {

    public AbstractFirearm(Position3D position) {
        super(position);
    }


    public abstract P popProjectile();

    public abstract int countEmptyRounds();

    public abstract int loadAllRounds(ItemContainerAttr<P> ammoBox);

    public abstract Class<P> getAllowedProjectileType();
}
