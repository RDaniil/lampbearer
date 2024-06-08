package com.vdn.lampbearer.entites.item.firearm;

import com.vdn.lampbearer.attributes.RoundContainerAttr;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.entites.item.interfaces.Projectile;
import org.hexworks.zircon.api.data.Position3D;
import com.vdn.lampbearer.entites.item.projectile.Round;

public abstract class AbstractFirearm<P extends Projectile> extends AbstractItem {

    public AbstractFirearm(Position3D position) {
        super(position);
    }

    public abstract P popProjectile();

    public abstract int countEmptyRounds();

    public abstract int loadAllRounds(RoundContainerAttr ammoBox);

    public abstract int loadOneRound(Round round);

    public abstract Class<P> getAllowedProjectileType();
}
