package com.vdn.lampbearer.game.world;

import com.vdn.lampbearer.action.Action;
import com.vdn.lampbearer.attributes.Attribute;
import com.vdn.lampbearer.attributes.occupation.BlockOccupier;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.engine.Engine;
import com.vdn.lampbearer.game.engine.ScheduledEngine;
import com.vdn.lampbearer.game.world.block.GameBlock;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.game.GameArea;

import java.util.Map;
import java.util.Optional;

/**
 * Содержит всю карту из блоков
 */
public class World extends WorldDelegate implements GameArea<Tile, GameBlock> {

    private final Engine engine;


    public World(Size3D visibleSize, Size3D actualSize, Map<Position3D, GameBlock> startingBlocks) {
        super(visibleSize, actualSize, startingBlocks);
        engine = new ScheduledEngine();
    }


    public void addEntity(AbstractEntity entity, Position3D position3D) {
        GameBlock block = fetchBlockAtOrElse(position3D, (pos) -> {
            throw new IllegalArgumentException(
                    String.format("Position %s does not contain any blocks", pos)
            );
        });

        entity.setPosition(position3D);
        block.addEntity(entity);
        engine.addEntity(entity);
    }


    public void updateBlockContent(Position3D position3D) {
        GameBlock block = fetchBlockAtOrElse(position3D, (pos) -> {
            throw new IllegalArgumentException(
                    String.format("Position %s does not contain any blocks", pos)
            );
        });

        block.updateContent();
    }


    public void removeEntity(AbstractEntity entity, Position3D position3D) {
        GameBlock block = fetchBlockAtOrElse(position3D, (pos) -> {
            throw new IllegalArgumentException(
                    String.format("Position %s does not contain any blocks", pos)
            );
        });

        block.removeEntity(entity);
        engine.removeEntity(entity);
    }


    public <T extends Attribute> Optional<AbstractEntity> getByAttribute(Position3D position,
                                                                         Class<T> attributeType) {
        try {
            GameBlock block = fetchBlockAtOrElse(position, (pos) -> {
                throw new IllegalArgumentException(
                        String.format("Position %s does not contain any blocks", pos)
                );
            });

            return block.getEntities().stream()
                    .filter(entity -> entity.findAttribute(attributeType).isPresent())
                    .findFirst();
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }


    public <T extends Action<?>> Optional<AbstractEntity> getByAction(Position3D position,
                                                                      Class<T> actionType) {
        try {
            GameBlock block = fetchBlockAtOrElse(position, (pos) -> {
                throw new IllegalArgumentException(
                        String.format("Position %s does not contain any blocks", pos)
                );
            });

            //TODO: Если мы хотим поднять предмет и их несколько - это не работает
            return block.getEntities().stream()
                    .filter(entity -> entity.findAction(actionType).isPresent())
                    .findFirst();
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }


    /**
     * Moves entity to the target position if it's not there yet
     *
     * @param entity    entity
     * @param targetPos target position
     * @return true if a move has been made
     */
    public boolean moveEntity(AbstractEntity entity, Position3D targetPos) {
        Position3D currentPos = entity.getPosition();
        var oldBlock = fetchBlockAtOrNull(currentPos);
        var newBlock = fetchBlockAtOrNull(targetPos);
        if (oldBlock == null || newBlock == null || currentPos.equals(targetPos)) return false;

        oldBlock.removeEntity(entity);
        entity.setPosition(targetPos);
        newBlock.addEntity(entity);
        return true;
    }


    public void update(GameContext gameContext) {
        engine.executeTurn(gameContext);
    }


    public void updateUI() {
        engine.updateUI();
    }


    public void initUi(GameContext gameContext) {
        engine.initUi(gameContext);
    }

    /**
     * Checks if a block at the position is walkable and there's no BlockOccupier
     *
     * @param position3D position
     * @return true if a block at the position is walkable and there's no BlockOccupier
     */
    public boolean isBlockWalkable(Position3D position3D) {
        try {
            GameBlock block = fetchBlockAtOrElse(position3D, (pos) -> {
                throw new IllegalArgumentException(
                        String.format("Position %s does not contains any blocks", pos)
                );
            });

            return block.isWalkable() && block.getEntities().stream()
                    .noneMatch(e -> e.findAttribute(BlockOccupier.class).isPresent());
        } catch (IllegalArgumentException e) {
            return false;
        }
    }


    /**
     * Checks if a block at the position is transparent
     *
     * @param position3D position
     * @return true if a block at the position is transparent
     */
    public boolean isBlockTransparent(Position3D position3D) {
        try {
            GameBlock block = fetchBlockAtOrElse(position3D, (pos) -> {
                throw new IllegalArgumentException(
                        String.format("Position %s does not contains any blocks", pos)
                );
            });

            return block.isTransparent();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
