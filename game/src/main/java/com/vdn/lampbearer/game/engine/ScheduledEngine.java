package com.vdn.lampbearer.game.engine;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Actor;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.entites.interfaces.Schedulable;
import com.vdn.lampbearer.entites.interfaces.Updatable;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.views.SidePanelView;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.uievent.KeyboardEvent;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
public class ScheduledEngine implements Engine {
    private final ArrayList<AbstractEntity> entities = new ArrayList<>();
    private SidePanelView sidePanelView;
    @Setter
    private EngineState state;


    public ScheduledEngine() {
        state = EngineState.GAME_LOOP;
    }


    public void addEntity(AbstractEntity entity) {
        entities.add(entity);
        if (entity instanceof Schedulable) {
            if(entity instanceof Updatable && !((Updatable) entity).needUpdate()){
                return;
            }
            addToSchedule((Schedulable) entity);
        }
    }


    public void removeEntity(AbstractEntity entity) {
        entities.remove(entity);
        if (entity instanceof Schedulable) {
            removeFromSchedule((Schedulable) entity);
        }
    }

    @Override
    public AbstractEntity findEntityByType(Class<? extends AbstractEntity> entityType) {
        Optional<AbstractEntity> first = entities.stream()
                .filter(entityType::isInstance).findFirst();
        return first.orElse(null);
    }


    public void addToSchedule(Schedulable schedulable) {
        Scheduler.add(schedulable);
    }


    public void removeFromSchedule(Schedulable schedulable) {
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

        if (state == EngineState.GAME_LOOP) {
            executeMainLoop(gameContext);
        } else if (state == EngineState.PAUSE) {
            executePausedPlayerAction(gameContext);
        }
    }


    private void executePausedPlayerAction(GameContext gameContext) {
        if (gameContext.getPlayer().makeAction(gameContext)) {
            executeMainLoop(gameContext);
        }
    }


    @SneakyThrows
    private void executeMainLoop(GameContext gameContext) {
        Schedulable nextSchedulable = peekNextSchedulable();
        boolean isPlayerActed = false;

        while (true) {
            if (nextSchedulable instanceof Actor) {
                boolean isActionDone = ((Actor<?>) nextSchedulable).makeAction(gameContext);
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
            } else if (nextSchedulable instanceof Updatable) {
                updateSchedule(nextSchedulable);

                Updatable updatable = (Updatable) nextSchedulable;
                if (updatable.needUpdate()) {
                    if (updatable.needToBeAnimated()) {
                        Thread.sleep(50);
                    }
                    updatable.update(gameContext);
                    log.info(Scheduler.currentTime + ": " + nextSchedulable + " updated");
                }

                nextSchedulable = peekNextSchedulable();
            }
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
