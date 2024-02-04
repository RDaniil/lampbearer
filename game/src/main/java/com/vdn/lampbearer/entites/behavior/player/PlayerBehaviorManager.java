package com.vdn.lampbearer.entites.behavior.player;

import com.vdn.lampbearer.action.reactions.LookReaction;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.engine.EngineState;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.hexworks.zircon.api.uievent.UIEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class PlayerBehaviorManager extends PlayerBehavior {

    //TODO: Реализовать SFM или типа того, сейчас тут могила опять
    private static final List<PlayerBehavior> POSSIBLE_BEHAVIORS = List.of(
            new PlayerInteractionBehavior(),
            new PlayerInventoryInteractionBehavior(),
            new PlayerMoveAndAttackBehavior(),
            new PlayerTargetBehavior()
    );

    private PlayerBehavior currentBehavior = new PlayerMoveAndAttackBehavior();

    private static final Set<KeyCode> MOVEMENT_KEYS = new HashSet<>(
            List.of(KeyCode.KEY_W, KeyCode.KEY_A, KeyCode.KEY_S, KeyCode.KEY_D, KeyCode.SPACE)
    );

    private static final Set<KeyCode> INVENTORY_KEYS = new HashSet<>(
            List.of(KeyCode.KEY_D, KeyCode.KEY_P, KeyCode.KEY_U)
    );

    private static final KeyCode INTERACTION_KEY = KeyCode.KEY_E;
    private static final KeyCode WAITING_KEY = KeyCode.SPACE;
    private static final Set<KeyCode> SPECIAL_KEYS = new HashSet<>(
            List.of(KeyCode.KEY_L, KeyCode.KEY_C, KeyCode.ESCAPE, KeyCode.ENTER)
    );


    public static boolean isValidEvent(KeyboardEvent keyboardEvent) {
        for (PlayerBehavior behavior : POSSIBLE_BEHAVIORS) {
            if (behavior.isUiEventApplicable(keyboardEvent)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean act(Player player, GameContext context) {
        var event = context.getEvent();
        if (event instanceof KeyboardEvent) {
            if (player.isStuck(context))
                throw new RuntimeException(String.format("%s is stuck!", player.getName()));

            if (!currentBehavior.isUiEventApplicable(event)) {
                currentBehavior = getNewCurrentBehavior(event);
                if (currentBehavior instanceof PlayerTargetBehavior) {
                    context.getWorld().setState(EngineState.PAUSE);
                }
            }

            return currentBehavior.act(player, context);
        }

        return false;
    }


    //TODO: Реализовать SFM или типа того, сейчас тут могила опять
    @NotNull
    private PlayerBehavior getNewCurrentBehavior(UIEvent event) {
        //TODO: мб чето поинтереснее придумать
        KeyboardEvent keyboardEvent = (KeyboardEvent) event;
        if (currentBehavior instanceof PlayerMoveAndAttackBehavior) {
            if (isInventoryAction(keyboardEvent)) {
                return new PlayerInventoryInteractionBehavior();
            }
            if (isInteraction(keyboardEvent)) {
                return new PlayerInteractionBehavior();
            }
            if (keyboardEvent.getCode().equals(KeyCode.KEY_L)) {
                return new PlayerTargetBehavior(new LookReaction());
            }
        }
        if (currentBehavior instanceof PlayerInventoryInteractionBehavior) {
            if (isMovement(keyboardEvent)) {
                return new PlayerMoveAndAttackBehavior();
            }
            if (isInteraction(keyboardEvent)) {
                return new PlayerInteractionBehavior();
            }
        }
        if (currentBehavior instanceof PlayerInteractionBehavior) {
            if (isMovement(keyboardEvent)) {
                return new PlayerMoveAndAttackBehavior();
            }
            if (isInteraction(keyboardEvent)) {
                return new PlayerInteractionBehavior();
            }
        }

        log.error("CANNOT PROCESS INPUT: " + keyboardEvent.getCode());
        return new PlayerMoveAndAttackBehavior();
    }


    public void changeBehavior(PlayerBehavior playerTargetBehavior, GameContext context) {
        if (currentBehavior instanceof PlayerTargetBehavior) {
            context.getWorld().setState(EngineState.GAME_LOOP);
            currentBehavior = playerTargetBehavior;
        }
    }


    public static boolean isMovement(KeyboardEvent event) {
        return event != null && MOVEMENT_KEYS.contains(event.getCode())
                && !event.getCtrlDown()
                && !event.getAltDown()
                && !event.getShiftDown();
    }


    public static boolean isInteraction(KeyboardEvent event) {
        return event != null && INTERACTION_KEY.equals(event.getCode())
                && !event.getCtrlDown()
                && !event.getAltDown()
                && !event.getShiftDown();
    }

    private static boolean isInventoryAction(KeyboardEvent event) {
        return event != null && INVENTORY_KEYS.contains(event.getCode());
    }

    @Override
    public boolean isUiEventApplicable(UIEvent event) {
        return true;
    }

}
