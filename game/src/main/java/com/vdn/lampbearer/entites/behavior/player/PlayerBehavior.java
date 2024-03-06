package com.vdn.lampbearer.entites.behavior.player;

import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.entites.behavior.Behavior;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public abstract class PlayerBehavior extends Behavior<Player> {

    private static final Set<KeyCode> MOVEMENT_KEYS = new HashSet<>(
            List.of(KeyCode.KEY_W, KeyCode.KEY_A, KeyCode.KEY_S, KeyCode.KEY_D, KeyCode.SPACE)
    );

    private static final Set<KeyCode> INVENTORY_KEYS = new HashSet<>(
            List.of(KeyCode.KEY_D, KeyCode.KEY_P, KeyCode.KEY_U)
    );

    private static final KeyCode INTERACTION_KEY = KeyCode.KEY_E;


    @NotNull
    @Override
    public abstract PlayerBehavior next(Player player, GameContext context);


    protected static boolean isMovement(KeyboardEvent event) {
        return event != null && MOVEMENT_KEYS.contains(event.getCode())
                && !event.getCtrlDown()
                && !event.getAltDown()
                && !event.getShiftDown();
    }


    protected static boolean isInteraction(KeyboardEvent event) {
        return event != null && INTERACTION_KEY.equals(event.getCode())
                && !event.getCtrlDown()
                && !event.getAltDown()
                && !event.getShiftDown();
    }


    protected static boolean isInventoryAction(KeyboardEvent event) {
        return event != null && INVENTORY_KEYS.contains(event.getCode())
                && !event.getCtrlDown()
                && !event.getAltDown()
                && !event.getShiftDown();
    }
}
