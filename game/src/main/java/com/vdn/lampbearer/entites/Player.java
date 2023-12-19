package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.attributes.BlockOccupier;
import com.vdn.lampbearer.attributes.HealthAttr;
import com.vdn.lampbearer.attributes.SpeedAttr;
import com.vdn.lampbearer.attributes.StrengthAttr;
import com.vdn.lampbearer.views.TileRepository;

import java.util.List;

public class Player extends AbstractEntity implements Schedulable {
    public Player() {
        setTile(TileRepository.PLAYER);
        setAttributes(List.of(
                new HealthAttr(10),
                new StrengthAttr(5),
                new SpeedAttr(5),
                new BlockOccupier()
        ));
    }

    @Override
    public int getTime() {
        return findAttribute(SpeedAttr.class).get().getValue();
    }
}
