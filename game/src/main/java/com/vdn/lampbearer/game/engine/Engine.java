package com.vdn.lampbearer.game.engine;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.game.GameContext;

public interface Engine {
    void addEntity(AbstractEntity entity);

    void removeEntity(AbstractEntity entity);

    void executeTurn(GameContext gameContext);

    void initUi(GameContext gameContext);

    void updateUI();
}
