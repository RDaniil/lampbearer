package com.vdn.lampbearer.exception;

import com.vdn.lampbearer.constants.GameOverResult;
import com.vdn.lampbearer.game.GameContext;
import lombok.Getter;

@Getter
public class GameOverException extends RuntimeException {
    private final GameContext gameContext;
    public final GameOverResult gameOverResult;

    public GameOverException(GameContext gameContext, GameOverResult gameOverResult) {
        this.gameContext = gameContext;
        this.gameOverResult = gameOverResult;
    }
}
