package com.vdn.lampbearer.entites.behavior;

import com.vdn.lampbearer.entites.Actor;
import com.vdn.lampbearer.game.GameContext;

/**
 * A behavior of Actor
 *
 * @param <T> type of actor
 */
public abstract class Behavior<T extends Actor<?>> {

    /**
     * Acts according to behavior
     *
     * @param actor   actor
     * @param context GameContext
     * @return true if an action has been made
     */
    public abstract boolean act(T actor, GameContext context);
}
