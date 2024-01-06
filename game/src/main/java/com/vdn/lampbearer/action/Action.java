package com.vdn.lampbearer.action;

import com.vdn.lampbearer.action.reactions.Reaction;

/**
 * An action which can be made on something/someone
 *
 * @param <T> Type of Reaction which has to be caused by it
 */
public interface Action<T extends Reaction> {

    Class<T> getReactionClass();

    default String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Возвращает реакцию на действие
     *
     * @return соответсвующая реакция
     * @throws RuntimeException если не удалось создать реакцию
     */
    default Reaction createReaction() {
        try {
            return getReactionClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
