package com.vdn.lampbearer.attributes.projectile;

import com.vdn.lampbearer.attributes.UsableAttr;
import lombok.Getter;

@Getter
public class PenetrationPowerAttr extends UsableAttr {
    public PenetrationPowerAttr(int penetrationPower) {
        super(penetrationPower);
    }
}
