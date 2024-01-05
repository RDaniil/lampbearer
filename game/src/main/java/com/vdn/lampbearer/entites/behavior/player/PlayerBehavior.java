package com.vdn.lampbearer.entites.behavior.player;

import com.vdn.lampbearer.action.AttackAction;
import com.vdn.lampbearer.action.interaction.Interaction;
import com.vdn.lampbearer.attributes.occupation.BlockOccupier;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.entites.behavior.Behavior;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.World;
import com.vdn.lampbearer.reactions.Reaction;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;

import java.util.*;

public class PlayerBehavior extends Behavior<Player> {

    private static final Set<KeyCode> MOVEMENT_KEYS = new HashSet<>(
            List.of(KeyCode.KEY_W, KeyCode.KEY_A, KeyCode.KEY_S, KeyCode.KEY_D)
    );

    private static final KeyCode INTERACTION_KEY = KeyCode.KEY_E;
    private static final KeyCode WAITING_KEY = KeyCode.SPACE;


    public static boolean isValidEvent(KeyboardEvent keyboardEvent) {
        //TODO: Нерасширяемое
        return isMovement(keyboardEvent)
                || isInteraction(keyboardEvent)
                || isWaiting(keyboardEvent);
    }


    @Override
    public boolean act(Player player, GameContext context) {
        //TODO: Разделить действия на тратящие время и не тратящие
        var event = context.getEvent();
        if (event instanceof KeyboardEvent) {
            if (player.isStuck(context))
                throw new RuntimeException(String.format("%s is stuck!", player.getName()));

            KeyboardEvent keyboardEvent = (KeyboardEvent) event;
            if (isMovement(keyboardEvent)) return move(context, keyboardEvent);
            if (isInteraction(keyboardEvent)) return interact(context);
            if (isWaiting(keyboardEvent)) return true;

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

        Map<KeyCode, AbstractEntity> keyToInteractableMap =
                getKeyToInteractableMap(player, context);
        if (keyToInteractableMap.isEmpty()) return false;

        AbstractEntity target = null;
        if (keyToInteractableMap.size() == 1) {
            target = keyToInteractableMap.values().stream().findFirst().get();
        } else {
            // todo WASD
            target = keyToInteractableMap.values().stream().findFirst().get();
        }

        if (target == null) throw new RuntimeException("No target's been found!");

        Reaction reaction = getReactionToInteraction(target);
        if (reaction == null) return false;

        return reaction.execute(player, target, context);
    }


    private static Reaction getReactionToInteraction(AbstractEntity target) {
        var interactionAction = target.findAction(Interaction.class);

        if (interactionAction.isEmpty()) throw new RuntimeException("No interaction's been found!");

        try {
            return interactionAction.get().createReaction();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param player  Player
     * @param context GameContext
     * @return map of surrounding objects which player can interact with, max 4 objects
     */
    private Map<KeyCode, AbstractEntity> getKeyToInteractableMap(Player player,
                                                                 GameContext context) {
        Map<KeyCode, Position3D> keyToPositionMap = player.getKeyToSurroundingPositionMap();

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
        var newPos = getNewPosition(player, event);

        var blockOccupier = context.getWorld().getByAttribute(newPos, BlockOccupier.class);
        if (blockOccupier.isPresent()) {
            AbstractEntity target = blockOccupier.get();

            return tryAttack(player, target, context);
        }

        if (context.getWorld().isBlockWalkable(newPos)) {
            if (context.getWorld().moveEntity(player, newPos)) {
                moveCamera(context, currentPos, newPos);
                return true;
            }
        }

        return false;
    }


    private boolean tryAttack(Player player, AbstractEntity target, GameContext context) {
        Optional<AttackAction> attackAction = target.findAction(AttackAction.class);
        if (attackAction.isPresent()) {
            try {
                return attackAction.get().createReaction().execute(player, target, context);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
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
     * @param player Player
     * @param event  KeyboardEvent
     * @return new player's position
     */
    private static Position3D getNewPosition(Player player, KeyboardEvent event) {
        Position3D newPosition = player.getKeyToSurroundingPositionMap().get(event.getCode());
        return newPosition != null ? newPosition : player.getPosition();
    }


    public static boolean isMovement(KeyboardEvent event) {
        return event != null && MOVEMENT_KEYS.contains(event.getCode());
    }


    public static boolean isInteraction(KeyboardEvent event) {
        return event != null && INTERACTION_KEY.equals(event.getCode());
    }


    private static boolean isWaiting(KeyboardEvent event) {
        return event != null && WAITING_KEY.equals(event.getCode());
    }
}