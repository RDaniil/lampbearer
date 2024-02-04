package com.vdn.lampbearer.entites.behavior.player;

import com.vdn.lampbearer.action.actions.AttackAction;
import com.vdn.lampbearer.attributes.occupation.BlockOccupier;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.World;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.hexworks.zircon.api.uievent.UIEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PlayerMoveAndAttackBehavior extends PlayerBehavior {

    private static final Set<KeyCode> MOVEMENT_KEYS = new HashSet<>(
            List.of(KeyCode.KEY_W, KeyCode.KEY_A, KeyCode.KEY_S, KeyCode.KEY_D, KeyCode.SPACE)
    );


    @Override
    public boolean act(Player actor, GameContext context) {
        var event = context.getEvent();
        if (event instanceof KeyboardEvent) {
            KeyboardEvent keyboardEvent = (KeyboardEvent) event;
            return move(context, keyboardEvent);
        }
        return false;
    }


    /**
     * Move player to one of 4 surrounding tiles
     *
     * @param context GameContext
     * @param event   KeyboardEvent
     * @return true if a move has been made
     */
    private boolean move(GameContext context, KeyboardEvent event) {
        if (event.getCode().equals(KeyCode.SPACE)) {
            return true;
        }

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
            return attackAction.get().createReaction().execute(player, target, context);
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


    @Override
    public boolean isUiEventApplicable(UIEvent event) {
        KeyboardEvent keyboardEvent = (KeyboardEvent) event;
        return keyboardEvent != null && MOVEMENT_KEYS.contains(keyboardEvent.getCode())
                && !keyboardEvent.getCtrlDown()
                && !keyboardEvent.getAltDown()
                && !keyboardEvent.getShiftDown();
    }
}
