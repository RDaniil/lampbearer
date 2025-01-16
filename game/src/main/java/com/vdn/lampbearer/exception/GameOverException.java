package com.vdn.lampbearer.exception;

import com.vdn.lampbearer.game.GameContext;

public class GameOverException extends RuntimeException {
    private final GameContext gameContext;

    public GameOverException(GameContext gameContext) {
        this.gameContext = gameContext;
    }
}
