package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.attributes.BlockOccupier;
import com.vdn.lampbearer.attributes.HealthAttr;
import com.vdn.lampbearer.attributes.SpeedAttr;
import com.vdn.lampbearer.attributes.StrengthAttr;
import com.vdn.lampbearer.entites.behavior.Behavior;
import com.vdn.lampbearer.entites.behavior.PlayerBehavior;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.views.TileRepository;

import java.util.List;

public class Player extends Actor implements Schedulable {

    //TODO: По идее это должен быть какой-то дженерик список, либо поведения могут вызывать другие поведения
    private final Behavior behavior = new PlayerBehavior();


    public Player() {
        setName("Lampbearer");
        setTile(TileRepository.PLAYER);
        setAttributes(List.of(
                new HealthAttr(10),
                new StrengthAttr(5),
                new SpeedAttr(5),
                new BlockOccupier()
        ));
    }


    @Override
    public void doAction(GameContext context) {
        behavior.act(this, context);
    }


    @Override
    public int getTime() {
        return findAttribute(SpeedAttr.class).get().getValue();
    }
}
