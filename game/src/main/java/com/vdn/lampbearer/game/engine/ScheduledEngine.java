package com.vdn.lampbearer.game.engine;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Actor;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.entites.behavior.player.PlayerBehavior;
import com.vdn.lampbearer.entites.interfaces.Schedulable;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.views.SidePanelView;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.uievent.KeyboardEvent;

import java.util.ArrayList;

@Slf4j
public class ScheduledEngine implements Engine {
    private final ArrayList<AbstractEntity> entities = new ArrayList<>();
    private SidePanelView sidePanelView;


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


    private void updateSchedule(Schedulable schedulable) {
        removeFromSchedule(schedulable);
        addToSchedule(schedulable);
    }


    public Schedulable peekNextSchedulable() {
        return Scheduler.peek();
    }


    @SneakyThrows
    @Override
    public void executeTurn(GameContext gameContext) {
        var event = gameContext.getEvent();

        if (!(event instanceof KeyboardEvent)) {
            return;
        }

        KeyboardEvent keyboardEvent = (KeyboardEvent) event;

        if (!PlayerBehavior.isValidEvent(keyboardEvent)) {
            return;
        }

        Schedulable nextSchedulable = peekNextSchedulable();
        boolean isPlayerActed = false;
        //Источники света здесь отдельно обрабатываются (тратим бензин каждый ход),
        // либо делать лежащие на полу фонари актерами

        while (nextSchedulable instanceof Actor) {
            boolean isActionDone = ((Actor<?>) nextSchedulable).makeAction(gameContext);
//            Thread.sleep(50);
            log.info(Scheduler.currentTime + ": " + nextSchedulable + " makes a move");
            if (nextSchedulable instanceof Player) {
                //Если игрок ничего не сделал - не нужно давать ход всем остальным. Нужно снова дать
                // ход игроку
                if (!isActionDone) {
                    break;
                } else {
                    isPlayerActed = true;
                }
            }

            updateSchedule(nextSchedulable);

            nextSchedulable = peekNextSchedulable();
            if (nextSchedulable instanceof Player && isPlayerActed) break;
        }
    }


    @Override
    public void initUi(GameContext gameContext) {
        sidePanelView = new SidePanelView(gameContext);
    }

    @Override
    public void updateUI() {
        sidePanelView.updateUI();
    }
}
