package com.vdn.lampbearer.action;

/**
 * An Action which has to be initialized by special interaction KEY
 *
 * @param <T> Type of Reaction which has to be caused by it
 */
public interface Interaction<T extends Reaction> extends Action<T> {
}
