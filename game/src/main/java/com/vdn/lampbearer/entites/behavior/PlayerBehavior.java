package com.vdn.lampbearer.entites.behavior;

import com.vdn.lampbearer.actions.AttackAction;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.World;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerBehavior implements Behavior {

    private static final Set<KeyCode> MOVEMENT_KEYS = new HashSet<>(
            List.of(KeyCode.KEY_W, KeyCode.KEY_A, KeyCode.KEY_S, KeyCode.KEY_D)
    );


    @Override
    public void act(AbstractEntity entity, GameContext context) {
        //TODO: Разделить действия на тратящие время и не тратящие
        var event = context.getEvent();
        if (event instanceof KeyboardEvent) {
            if (isMovement((KeyboardEvent) event)) {
                move(context, (KeyboardEvent) event);
            }
        }
    }


    private void move(GameContext context, KeyboardEvent event) {
        Player player = context.getPlayer();
        var currentPos = player.getPosition();
        var newPos = getNewPosition(event, currentPos);

        var blockOccupier = context.getWorld().getBlockOccupier(newPos);
        if (blockOccupier.isPresent()) {
            new AttackAction().execute(player, blockOccupier.get(), context);
            return;
        }

        if (context.getWorld().moveEntity(player, newPos))
            moveCamera(context, currentPos, newPos);
    }


    private void moveCamera(GameContext gameContext, Position3D previousPos, Position3D currentPos) {
        World world = gameContext.getWorld();
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
}
