package com.vdn.lampbearer.game.world;

import com.vdn.lampbearer.action.Action;
import com.vdn.lampbearer.attributes.Attribute;
import com.vdn.lampbearer.attributes.occupation.BlockOccupier;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.BlockEntity;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.engine.Engine;
import com.vdn.lampbearer.game.engine.EngineState;
import com.vdn.lampbearer.game.engine.ScheduledEngine;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.light.Light;
import com.vdn.lampbearer.services.light.LightingService;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.game.GameArea;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Содержит всю карту из блоков
 */
@Slf4j
public class World extends WorldDelegate implements GameArea<Tile, GameBlock> {

    private final Engine engine;
    private final LightingService lightingService;
    private Player player;


    public World(Size3D visibleSize, Size3D actualSize, Map<Position3D, GameBlock> startingBlocks) {
        super(visibleSize, actualSize, startingBlocks);
        engine = new ScheduledEngine();

        lightingService = new LightingService(this, startingBlocks);
    }


    public void addDynamicLight(AbstractEntity entity, Light dynamicLight) {
        lightingService.addDynamicLight(entity, dynamicLight);
    }


    public void removeDynamicLight(AbstractEntity entity, Light dynamicLight) {
        lightingService.removeDynamicLight(entity, dynamicLight);
    }


    public void removeLight(Light light) {
        lightingService.removeLight(light);
    }


    public void removeDynamicLightByEntity(AbstractEntity entity) {
        lightingService.removeDynamicLight(entity);
    }


    public void addStaticLight(Light staticLight) {
        lightingService.addStaticLight(staticLight);
    }


    public void removeStaticLight(Light staticLight) {
        lightingService.removeStaticLight(staticLight);
    }


    private boolean isEntityContainsLight(AbstractEntity entity) {
        return lightingService.isEntityContainsLight(entity);
    }


    private void moveDynamicLightWithEntity(AbstractEntity entity) {
        lightingService.moveDynamicLightWithEntity(entity);
    }


    public AbstractEntity getEntityByLight(Light light) {
        return lightingService.getEntityByLight(light);
    }


    public void addPlayer(Player player, Position3D position3D) {
        this.player = player;
        addEntity(player, position3D);
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


    /**
     * Удаляет сущность с карты мира. Сущность все еще может существовать (например перемещена с
     * карты в инвентарь)
     *
     * @param entity     Удаляемая сущность
     * @param position3D Позиция удаляемой сущности
     */
    public void removeEntity(AbstractEntity entity, Position3D position3D) {
        GameBlock block = fetchBlockAtOrElse(position3D, (pos) -> {
            throw new IllegalArgumentException(
                    String.format("Position %s does not contain any blocks", pos)
            );
        });

        block.removeEntity(entity);
        removeDynamicLightByEntity(entity);
    }


    /**
     * Удаляет сущность и из движка и с карты. Сущность больше не может нигде существовать
     *
     * @param entity     Удаляемая сущность
     * @param position3D Позиция удаляемой сущности
     */
    public void deleteEntity(AbstractEntity entity, Position3D position3D) {
        GameBlock block = fetchBlockAtOrElse(position3D, (pos) -> {
            throw new IllegalArgumentException(
                    String.format("Position %s does not contain any blocks", pos)
            );
        });

        block.removeEntity(entity);
        engine.removeEntity(entity);
        removeDynamicLightByEntity(entity);
    }


    public void removeFromSchedule(AbstractEntity entity) {
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
            List<AbstractEntity> entitiesByAction = block.getEntities().stream()
                    .filter(entity -> entity.findAction(actionType).isPresent())
                    .collect(Collectors.toList());
            if (entitiesByAction.size() > 1) {
                throw new IllegalArgumentException(
                        "Ожидали найти 1 сущность по Action " + actionType.getSimpleName());
            }
            return entitiesByAction.isEmpty() ? Optional.empty() :
                    Optional.of(entitiesByAction.get(0));
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

        if (isEntityContainsLight(entity)) {
            moveDynamicLightWithEntity(entity);
            //TODO: Оптимизировать, сейчас отрисовываем весь свет сразу после каждого движения +
            // после того как все сделали ход

            updateLighting();
        }
        return true;
    }


    public void updateLighting() {
        lightingService.updateLighting(player);
    }


    public void update(GameContext gameContext) {
        double startTime = (double) System.nanoTime();
        engine.executeTurn(gameContext);
        double executeTurn = (double) System.nanoTime();
        log.info("executeTurn TIME: " + (executeTurn - startTime) / (1000_000));
        updateLighting();
        double endTime = (double) System.nanoTime();

        log.info("LIGHTING TIME: " + (endTime - executeTurn) / (1000_000));
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


    public void setState(EngineState engineState) {
        engine.setState(engineState);
    }


    /**
     * Возвращает сущность(и) по указанной позиции.
     *
     * @param selectedPosition позиция, с которой запрашивается сущность(и)
     * @return List<AbstractEntity>, пустой если сущность(и) нет
     */
    public List<AbstractEntity> getEntitiesAt(Position selectedPosition) {
        try {
            GameBlock block = fetchBlockAtOrElse(selectedPosition.toPosition3D(0),
                    (pos) -> {
                        throw new IllegalArgumentException(
                                String.format("Position %s does not contains any blocks", pos)
                        );
                    });
            return block.getEntities();
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }


    /**
     * Возвращает сущность по указанной позиции. Если на указанной позиции сущности не существует -
     * создается BlockEntity - информация о блоке в виде сущности
     *
     * @param selectedPosition позиция, с которой запрашивается сущность
     * @return сущность, реальная или сущность-блок. null, если переданна позция, по которой не
     * существует блоков
     */
    public AbstractEntity getEntityOrBlockEntityAt(Position selectedPosition) {
        try {
            GameBlock block = fetchBlockAtOrElse(selectedPosition.toPosition3D(0),
                    (pos) -> {
                        throw new IllegalArgumentException(
                                String.format("Position %s does not contains any blocks", pos)
                        );
                    });
            if (block.isUnseen()) {
                return new BlockEntity(selectedPosition,
                        "It's dark", "I can't see there");
            }
            if (block.getEntities().isEmpty()) {
                return new BlockEntity(selectedPosition, block);
            }
            //TODO: Если на одной клеточке больше 1 сущности - не работает
            return block.getEntities().get(0);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    /**
     * @param position3D позиция
     * @return до 4-х позиций вокруг, находящихся в пределах игрового поля, на которые можно встать
     */
    public List<Position3D> getWalkablePositionsAround(Position3D position3D) {
        return Stream.of(
                        position3D.withRelativeY(-1),
                        position3D.withRelativeX(-1),
                        position3D.withRelativeY(1),
                        position3D.withRelativeX(1)
                ).filter(p -> {
                    GameBlock block = this.fetchBlockAtOrNull(position3D);
                    return block != null && block.isWalkable();
                })
                .collect(Collectors.toList());
    }
}
