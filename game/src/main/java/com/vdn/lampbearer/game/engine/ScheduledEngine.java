package com.vdn.lampbearer.game.engine;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Actor;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.entites.Schedulable;
import com.vdn.lampbearer.entites.behavior.PlayerBehavior;
import com.vdn.lampbearer.game.GameContext;
import org.hexworks.zircon.api.uievent.KeyboardEvent;

import java.util.ArrayList;

public class ScheduledEngine implements Engine {
    private final ArrayList<AbstractEntity> entities = new ArrayList<>();


    public void addEntity(AbstractEntity entity) {
        entities.add(entity);
        if (entity instanceof Schedulable) {
            addToSchedule((Schedulable) entity);
        }
    }


    public void removeEntity(AbstractEntity entity) {
        entities.add(entity);
        if (entity instanceof Schedulable) {
            removeFromSchedule((Schedulable) entity);
        }
    }


    private void addToSchedule(Schedulable schedulable) {
        Scheduler.add(schedulable);
    }


    private void removeFromSchedule(Schedulable schedulable) {
        Scheduler.remove(schedulable);
    }


    public Schedulable getNextSchedulable() {
        return Scheduler.getNext();
    }


    @Override
    public void executeTurn(GameContext gameContext) {
        var event = gameContext.getEvent();

        if (event instanceof KeyboardEvent) {
            if (PlayerBehavior.isMovement((KeyboardEvent) event)) {

                Schedulable nextSchedulable = getNextSchedulable();

                while (nextSchedulable instanceof Actor) {
                    ((Actor) nextSchedulable).doAction(gameContext);
                    addToSchedule(nextSchedulable);
                    if (nextSchedulable instanceof Player) break;

                    nextSchedulable = getNextSchedulable();
                }
            }
        }
    }
}
