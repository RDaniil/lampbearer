package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.game.GameContext;

public abstract class Actor extends AbstractEntity {
    public abstract void doAction(GameContext context);
}
