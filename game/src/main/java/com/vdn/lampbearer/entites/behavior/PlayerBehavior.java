package com.vdn.lampbearer.entites.behavior;

import com.vdn.lampbearer.action.Action;
import com.vdn.lampbearer.action.AttackAction;
import com.vdn.lampbearer.action.interaction.Interaction;
import com.vdn.lampbearer.attributes.BlockOccupier;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.World;
import com.vdn.lampbearer.reactions.AttackReaction;
import com.vdn.lampbearer.reactions.Reaction;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;

import java.util.*;

public class PlayerBehavior implements Behavior {

    private static final Set<KeyCode> MOVEMENT_KEYS = new HashSet<>(
            List.of(KeyCode.KEY_W, KeyCode.KEY_A, KeyCode.KEY_S, KeyCode.KEY_D)
    );

    private static final KeyCode INTERACTION_KEY = KeyCode.KEY_E;


    @Override
    public boolean act(AbstractEntity entity, GameContext context) {
        //TODO: Разделить действия на тратящие время и не тратящие
        var event = context.getEvent();
        if (event instanceof KeyboardEvent) {
            KeyboardEvent keyboardEvent = (KeyboardEvent) event;
            if (isMovement(keyboardEvent)) return move(context, keyboardEvent);
            if (isInteraction(keyboardEvent)) return interact(context);

            return false;
        }

        return false;
    }


    /**
     * Interact with 4 surrounding tiles
     *
     * @param context GameContext
     * @return true if an interaction has been made
     */
    private boolean interact(GameContext context) {
        Player player = context.getPlayer();
        var currentPos = player.getPosition();

        Map<KeyCode, AbstractEntity> keyToInteractableMap =
                getKeyToInteractableMap(context, currentPos);
        if (keyToInteractableMap.isEmpty()) return false;

        AbstractEntity target = null;
        if (keyToInteractableMap.size() == 1) {
            target = keyToInteractableMap.values().stream().findFirst().get();
        } else {
            // todo WASD
            target = keyToInteractableMap.values().stream().findFirst().get();
        }

        if (target == null) throw new RuntimeException("No target's been found!");

        Reaction reaction = getReactionToInteraction(target.getActions());
        if (reaction == null) return false;

        return reaction.execute(player, target, context);
    }


    private static Reaction getReactionToInteraction(List<Action<?>> actions) {
        Optional<Action<?>> action = actions.stream()
                .filter(Interaction.class::isInstance).findFirst();
        if (action.isEmpty()) throw new RuntimeException("No interaction's been found!");

        try {
            return action.get().createReaction();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param context  GameContext
     * @param position player's position
     * @return map of surrounding objects which player can interact with, max 4 objects
     */
    private Map<KeyCode, AbstractEntity> getKeyToInteractableMap(GameContext context,
                                                                 Position3D position) {
        Map<KeyCode, Position3D> keyToPositionMap = new HashMap<>();
        keyToPositionMap.put(KeyCode.KEY_W, position.withRelativeY(-1));
        keyToPositionMap.put(KeyCode.KEY_A, position.withRelativeX(-1));
        keyToPositionMap.put(KeyCode.KEY_S, position.withRelativeY(1));
        keyToPositionMap.put(KeyCode.KEY_D, position.withRelativeX(1));

        Map<KeyCode, AbstractEntity> keyToInteractableMap = new HashMap<>();
        for (Map.Entry<KeyCode, Position3D> keyCodePosition : keyToPositionMap.entrySet()) {
            Optional<AbstractEntity> block =
                    context.getWorld().getByAction(keyCodePosition.getValue(), Interaction.class);
            if (block.isEmpty()) continue;

            keyToInteractableMap.put(keyCodePosition.getKey(), block.get());
        }

        return keyToInteractableMap;
    }


    /**
     * Move player to one of 4 surrounding tiles
     *
     * @param context GameContext
     * @param event   KeyboardEvent
     * @return true if a move has been made
     */
    private boolean move(GameContext context, KeyboardEvent event) {
        Player player = context.getPlayer();
        var currentPos = player.getPosition();
        var newPos = getNewPosition(event, currentPos);

        var blockOccupier = context.getWorld().getByAttribute(newPos, BlockOccupier.class);
        if (blockOccupier.isPresent()) {
            AbstractEntity target = blockOccupier.get();

            if (target.findAction(AttackAction.class).isPresent())
                return new AttackReaction().execute(player, target, context);

            return false;
        }

        if (context.getWorld().isBlockWalkable(newPos)) {
            if (context.getWorld().moveEntity(player, newPos)) {
                moveCamera(context, currentPos, newPos);
                return true;
            }
        }

        return false;
    }


    /**
     * Move camera the way to keep the player in the middle of the world
     *
     * @param context     GameContext
     * @param previousPos previous player's position
     * @param currentPos  current player's position
     */
    private void moveCamera(GameContext context, Position3D previousPos, Position3D currentPos) {
        World world = context.getWorld();
        var halfHeight = world.getVisibleSize().getYLength() / 2;
        var halfWidth = world.getVisibleSize().getXLength() / 2;
        var screenPos = currentPos.minus(world.getVisibleOffset());

        if (previousPos.getY() > currentPos.getY() && screenPos.getY() < halfHeight) {
            world.scrollOneBackward();
        }
        if (previousPos.getY() < currentPos.getY() && screenPos.getY() > halfHeight) {
            world.scrollOneForward();
        }
        if (previousPos.getX() > currentPos.getX() && screenPos.getX() < halfWidth) {
            world.scrollOneLeft();
        }
        if (previousPos.getX() < currentPos.getX() && screenPos.getX() > halfWidth) {
            world.scrollOneRight();
        }
    }


    /**
     * Get new player's position according to pressed key
     *
     * @param event      KeyboardEvent
     * @param currentPos current player's position
     * @return new player's position
     */
    private static Position3D getNewPosition(KeyboardEvent event, Position3D currentPos) {
        Position3D newPosition;
        switch (event.getCode()) {
            case KEY_W:
                newPosition = currentPos.withRelativeY(-1);
                break;
            case KEY_A:
                newPosition = currentPos.withRelativeX(-1);
                break;
            case KEY_S:
                newPosition = currentPos.withRelativeY(1);
                break;
            case KEY_D:
                newPosition = currentPos.withRelativeX(1);
                break;
            default:
                newPosition = currentPos;
                break;
        }

        return newPosition;
    }


    public static boolean isMovement(KeyboardEvent event) {
        return event != null && MOVEMENT_KEYS.contains(event.getCode());
    }


    public static boolean isInteraction(KeyboardEvent event) {
        return event != null && INTERACTION_KEY.equals(event.getCode());
    }
}
