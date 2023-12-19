package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.attributes.BlockOccupier;
import com.vdn.lampbearer.attributes.HealthAttr;
import com.vdn.lampbearer.attributes.SpeedAttr;
import com.vdn.lampbearer.attributes.StrengthAttr;
import com.vdn.lampbearer.entites.behavior.Behavior;
import com.vdn.lampbearer.entites.behavior.WanderBehavior;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.views.TileRepository;

import java.util.List;

public class SimpleZombie extends Actor implements Schedulable {

    public SimpleZombie(SpeedAttr speedAttr) {
        setTile(TileRepository.SIMPLE_ZOMBIE);
        setAttributes(List.of(
                new HealthAttr(20),
                new StrengthAttr(5),
                speedAttr,
                new BlockOccupier()
        ));
    }

    //TODO: По идее это должен быть какой-то дженерик список, либо поведения могут вызывать другие поведения
    Behavior wanderBehavior = new WanderBehavior();

    @Override
    public void doAction(GameContext context) {
        wanderBehavior.Act(this, context);
    }

    @Override
    public int getTime() {
        return findAttribute(SpeedAttr.class).get().getValue();
    }
}
