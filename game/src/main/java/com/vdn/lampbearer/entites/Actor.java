package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.entites.behavior.Behavior;
import com.vdn.lampbearer.game.GameContext;
import org.hexworks.zircon.api.data.Position3D;

import java.util.ArrayList;
import java.util.List;

/**
 * An actor who can make some actions
 *
 * @param <B> type of it's behavior
 */
public abstract class Actor<B extends Behavior<?>> extends AbstractEntity {

    protected final ArrayList<B> behaviors = new ArrayList<>();

    private final List<Position3D> surroundingPositions = new ArrayList<>();


    /**
     * Makes an action
     *
     * @param context GameContext
     * @return true if an action has been made
     */
    public abstract boolean makeAction(GameContext context);

    /**
     * Checks if an actor is stuck
     *
     * @param context GameContext
     * @return true if an actor is stuck
     */
    public abstract boolean isStuck(GameContext context);


    /**
     * @return 4 surrounding positions (they may be out of the world)
     */
    public List<Position3D> getSurroundingPositions() {
        surroundingPositions.clear();
        surroundingPositions.add(position.withRelativeY(-1));
        surroundingPositions.add(position.withRelativeX(-1));
        surroundingPositions.add(position.withRelativeY(1));
        surroundingPositions.add(position.withRelativeX(1));
        return surroundingPositions;
    }
}
