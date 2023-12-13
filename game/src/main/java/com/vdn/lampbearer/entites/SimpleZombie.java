package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.entites.behavior.Behavior;
import com.vdn.lampbearer.entites.behavior.WanderBehavior;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.views.TileRepository;

public class SimpleZombie extends Actor implements Schedulable {

    private int speed;

    public SimpleZombie(int speed) {
        setTile(TileRepository.SIMPLE_ZOMBIE);
        this.speed = speed;

    }

    //TODO: По идее это должен быть какой-то дженерик список, либо поведения могут вызывать другие поведения
    Behavior wanderBehavior = new WanderBehavior();

    @Override
    public void doAction(GameContext context) {
        wanderBehavior.Act(this, context);
    }

    @Override
    public int getTime() {
        return speed;
    }
}
