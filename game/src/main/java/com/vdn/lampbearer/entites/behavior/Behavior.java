package com.vdn.lampbearer.entites.behavior;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.game.GameContext;

public interface Behavior {
    void Act(AbstractEntity entity, GameContext context);
}
