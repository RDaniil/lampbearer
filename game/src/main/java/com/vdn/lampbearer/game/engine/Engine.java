package com.vdn.lampbearer.game.engine;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.interfaces.Schedulable;
import com.vdn.lampbearer.game.GameContext;

public interface Engine {
    void addEntity(AbstractEntity entity);

    void addToSchedule(Schedulable schedulable);

    void removeFromSchedule(Schedulable schedulable);

    void removeEntity(AbstractEntity entity);

    AbstractEntity findEntityByType(Class<? extends AbstractEntity> entityType);

    void executeTurn(GameContext gameContext);

    void initUi(GameContext gameContext);

    void updateUI();

    void setState(EngineState engineState);
}
