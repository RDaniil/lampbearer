package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.attributes.HealthAttr;
import com.vdn.lampbearer.attributes.SpeedAttr;
import com.vdn.lampbearer.entites.behavior.Behavior;
import com.vdn.lampbearer.game.GameContext;
import org.hexworks.zircon.api.data.Position3D;

import java.util.*;

/**
 * An actor who can make some actions
 *
 * @param <B> type of it's behavior
 */
public abstract class Actor<B extends Behavior<?>> extends AbstractEntity {

    /**
     * Доступные поведения
     */
    protected final Set<B> behaviors = new HashSet<>();

    private final List<Position3D> surroundingPositions = new ArrayList<>();


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(" ");
        Optional<SpeedAttr> speed = findAttribute(SpeedAttr.class);
        Optional<HealthAttr> health = findAttribute(HealthAttr.class);
        speed.ifPresent(speedAttr -> sb.append("S:").append(speedAttr.getValue()));
        sb.append(", P:").append(getPosition());
        health.ifPresent(healthAttr -> sb.append(
                String.format(", H:%s/%s", healthAttr.getHealth(), healthAttr.getMaxHealth())
        ));
        return sb.toString();
    }


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
     * @param behaviorClass класс поведения
     * @return экземпляр поведения, если оно доступно, иначе null
     */
    public B findBehavior(Class<? extends B> behaviorClass) {
        return behaviors.stream().filter(b -> (b.getClass().equals(behaviorClass)))
                .findFirst().orElse(null);
    }


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
