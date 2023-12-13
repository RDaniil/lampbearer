package com.vdn.lampbearer.game.engine;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.World;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;

import java.util.ArrayList;

public class BasicEngine implements Engine {
    private final ArrayList<AbstractEntity> entities = new ArrayList<>();

    public void addEntity(AbstractEntity entity) {
        entities.add(entity);
    }

    public void removeEntity(AbstractEntity entity) {
        entities.add(entity);
    }

    @Override
    public void executeTurn(GameContext gameContext) {
        var event = gameContext.getEvent();
        Player player = gameContext.getPlayer();
        var currentPos = player.getPosition();
        Position3D newPosition;
        if (event instanceof KeyboardEvent) {
            if (isMovementEvent((KeyboardEvent) event)) {
                //TODO: По идее передвижение и камера не должны тут обрабатываться
                newPosition = getNewPosition((KeyboardEvent) event, currentPos);
                if (gameContext.getWorld().moveEntity(player, newPosition)) {
                    moveCamera(gameContext, currentPos, newPosition);
                }
            }
        }
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

    private boolean isMovementEvent(KeyboardEvent event) {
        return event.getCode() == KeyCode.KEY_W ||
                event.getCode() == KeyCode.KEY_A ||
                event.getCode() == KeyCode.KEY_S ||
                event.getCode() == KeyCode.KEY_D;
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
}
