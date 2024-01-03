package com.vdn.lampbearer.game.engine;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Actor;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.entites.Schedulable;
import com.vdn.lampbearer.entites.behavior.player.PlayerBehavior;
import com.vdn.lampbearer.game.GameContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.uievent.KeyboardEvent;

import java.util.ArrayList;

@Slf4j
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


    public Schedulable peekNextSchedulable() {
        return Scheduler.peek();
    }


    @SneakyThrows
    @Override
    public void executeTurn(GameContext gameContext) {
        var event = gameContext.getEvent();

        if (event instanceof KeyboardEvent) {
            KeyboardEvent keyboardEvent = (KeyboardEvent) event;
            if (PlayerBehavior.isValidEvent(keyboardEvent)) {

                Schedulable nextSchedulable = peekNextSchedulable();

                while (nextSchedulable instanceof Actor) {
                    boolean isActionDone = ((Actor) nextSchedulable).makeAction(gameContext);
                    Thread.sleep(55);
                    log.info(nextSchedulable + " makes a move");
                    if (!isActionDone && nextSchedulable instanceof Player) break;

                    removeFromSchedule(nextSchedulable);
                    addToSchedule(nextSchedulable);
                    if (nextSchedulable instanceof Player) break;

                    nextSchedulable = peekNextSchedulable();
                }
            }
        }
    }
}
