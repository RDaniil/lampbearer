package com.vdn.lampbearer.action.reactions;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.game.GameContext;

/**
 * A reaction to a certain Action
 */
public interface Reaction {
    boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context);
}
