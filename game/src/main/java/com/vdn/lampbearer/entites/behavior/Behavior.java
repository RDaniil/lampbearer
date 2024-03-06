package com.vdn.lampbearer.entites.behavior;

import com.vdn.lampbearer.entites.Actor;
import com.vdn.lampbearer.game.GameContext;
import lombok.NonNull;

/**
 * A behavior of Actor
 *
 * @param <A> type of actor
 */
public abstract class Behavior<A extends Actor<?>> {

    /**
     * Реализация конечного автомата
     *
     * @param actor   действующее лицо
     * @param context контекст
     * @return следующее поведение
     */
    @NonNull
    public abstract Behavior<?> next(A actor, GameContext context);

    /**
     * Acts according to behavior
     *
     * @param actor   actor
     * @param context GameContext
     * @return true if an action has been made
     */
    public abstract boolean act(A actor, GameContext context);
}
