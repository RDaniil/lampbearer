package com.vdn.lampbearer.entites.behavior.player;

import com.vdn.lampbearer.action.Interaction;
import com.vdn.lampbearer.action.Reaction;
import com.vdn.lampbearer.action.reactions.LookReaction;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.game.GameContext;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PlayerInteractionBehavior extends PlayerBehavior {

    @Override
    public boolean act(Player actor, GameContext context) {
        return interact(context);
    }


    @NotNull
    @Override
    public PlayerBehavior next(Player player, GameContext context) {
        var event = context.getEvent();
        if (!(event instanceof KeyboardEvent)) return this;

        KeyboardEvent keyboardEvent = (KeyboardEvent) event;

        if (isMovement(keyboardEvent)) return new PlayerMoveAndAttackBehavior();
        if (isInventoryAction(keyboardEvent)) return new PlayerInventoryInteractionBehavior();
        if (isInteraction(keyboardEvent)) return this;
        if (keyboardEvent.getCode().equals(KeyCode.KEY_L))
            return new PlayerTargetBehavior(new LookReaction());

        return this;
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

        return interactionAction.get().createReaction();
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
}
