package com.vdn.lampbearer.entites.interfaces;

import com.vdn.lampbearer.game.GameContext;

/**
 * Сущность, сосотояние которой нужно постоянно обновлять
 */
public interface Updateable {

    void update(GameContext context);
}
