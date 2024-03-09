package com.vdn.lampbearer.entites.interfaces;

import com.vdn.lampbearer.game.GameContext;

/**
 * Сущность, состояние которой нужно постоянно обновлять
 */
public interface Updatable extends Schedulable {

    void update(GameContext context);

    boolean needUpdate();

    default boolean needToBeAnimated() {
        return false;
    }
}
