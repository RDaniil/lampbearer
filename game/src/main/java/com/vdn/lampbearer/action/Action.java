package com.vdn.lampbearer.action;

import com.vdn.lampbearer.reactions.Reaction;

/**
 * An action which can be made on something/someone
 *
 * @param <T> Type of Reaction which has to be caused by it
 */
public interface Action<T extends Reaction> {

    Class<T> getReactionClass();

    default Reaction createReaction() throws ReflectiveOperationException {
        return getReactionClass().getDeclaredConstructor().newInstance();
    }
}
