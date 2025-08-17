package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.action.actions.combat.AttackAction;
import com.vdn.lampbearer.attributes.creature.HealthAttr;
import com.vdn.lampbearer.attributes.creature.PerceptionAttr;
import com.vdn.lampbearer.attributes.creature.SpeedAttr;
import com.vdn.lampbearer.attributes.creature.StrengthAttr;
import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.attributes.occupation.StaticBlockOccupier;
import com.vdn.lampbearer.entites.behavior.player.PlayerBehavior;
import com.vdn.lampbearer.entites.behavior.player.PlayerMoveAndAttackBehavior;
import com.vdn.lampbearer.entites.behavior.player.PlayerTargetBehavior;
import com.vdn.lampbearer.entites.interfaces.Schedulable;
import com.vdn.lampbearer.entites.item.FirstAidKit;
import com.vdn.lampbearer.entites.item.LighthouseKey;
import com.vdn.lampbearer.entites.item.firearm.Revolver;
import com.vdn.lampbearer.entites.item.projectile.Stone;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.engine.EngineState;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.light.PlayerFOWSight;
import com.vdn.lampbearer.services.light.PlayerSight;
import com.vdn.lampbearer.utils.PositionUtils;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class Player extends Actor<PlayerBehavior> implements Schedulable {

    @Setter
    private PlayerBehavior behavior = new PlayerMoveAndAttackBehavior();

    /**
     * Зрение игрока без фонарей в тумане войны
     */
    private final PlayerFOWSight fowLight;

    /**
     * Зрение игрока
     */
    private final PlayerSight sight;


    public PlayerFOWSight getFowLight() {
        fowLight.setPosition(getPosition().to2DPosition());
        return fowLight;
    }


    private final Map<KeyCode, Position3D> keyToSurroundingPositionMap = new HashMap<>();


    public Player(Position3D position3D) {
        super(position3D);

        BlockType type = BlockType.PLAYER;
        GameBlock block = GameBlockFactory.returnGameBlock(type);
        setName(block.getName());
        setDescription(block.getDescription());
        setTile(TileRepository.getTile(type));
        InventoryAttr inventoryAttr = new InventoryAttr(10);
//        inventoryAttr.putItem(LighthouseKey.createForWorld(position3D));

        HealthAttr healthAttr = new HealthAttr(1000);
        healthAttr.reduceHealth(40);

        PerceptionAttr perceptionAttr = new PerceptionAttr(20);

        setAttributes(List.of(
                healthAttr,
                new StrengthAttr(5),
                new SpeedAttr(1),
                perceptionAttr,
                StaticBlockOccupier.getInstance(),
                inventoryAttr
        ));
        setActions(List.of(AttackAction.getInstance()));
        fowLight = new PlayerFOWSight(Position.create(0, 0), 3,
                TileColor.fromString("#FAF1C422"));
        sight = new PlayerSight(perceptionAttr);
    }


    @Override
    public boolean makeAction(GameContext context) {
        var event = context.getEvent();
        if (!(event instanceof KeyboardEvent)) return false;

        if (isStuck(context))
            throw new RuntimeException(String.format("%s is stuck!", getName()));

        if (behavior instanceof PlayerTargetBehavior) {
            context.getWorld().setState(EngineState.GAME_LOOP);
        }

        PlayerBehavior nextBehavior = behavior.next(this, context);
        if (nextBehavior instanceof PlayerTargetBehavior) {
            context.getWorld().setState(EngineState.PAUSE);
        }

//        log.error(String.format("%s -> %s", behavior.getClass().getSimpleName(), nextBehavior.getClass().getSimpleName()));

        return (behavior = nextBehavior).act(this, context);
    }


    @Override
    public boolean isStuck(GameContext context) {
        List<GameBlock> blocks = getSurroundingPositions().stream()
                .map(context.getWorld()::fetchBlockAtOrNull).filter(Objects::nonNull)
                .collect(Collectors.toList());

        for (GameBlock block : blocks) {
            if (!block.isWalkable()) continue;
            if (block.getEntities().isEmpty()) return false;

            for (AbstractEntity entity : block.getEntities()) {
                if (entity.findAttribute(StaticBlockOccupier.class).isEmpty() ||
                        entity.findAction(AttackAction.class).isPresent())
                    return false;
            }
        }

        return true;
    }


    @Override
    public int getTime() {
        return findAttribute(SpeedAttr.class).get().getValue();
    }


    public PlayerSight getSight() {
        sight.setPosition(getPosition().to2DPosition());
        return sight;
    }


    /**
     * @return map of key (WASD) to position (according to direction, it may be out of the world)
     */
    public Map<KeyCode, Position3D> getKeyToSurroundingPositionMap() {
        keyToSurroundingPositionMap.clear();
        keyToSurroundingPositionMap.put(KeyCode.KEY_W, position.withRelativeY(-1));
        keyToSurroundingPositionMap.put(KeyCode.KEY_A, position.withRelativeX(-1));
        keyToSurroundingPositionMap.put(KeyCode.KEY_S, position.withRelativeY(1));
        keyToSurroundingPositionMap.put(KeyCode.KEY_D, position.withRelativeX(1));
        return keyToSurroundingPositionMap;
    }


    public PositionUtils.Direction getDirection(KeyCode keyCode) {
        switch (keyCode) {
            case KEY_W:
                return PositionUtils.Direction.UP;
            case KEY_A:
                return PositionUtils.Direction.LEFT;
            case KEY_S:
                return PositionUtils.Direction.DOWN;
            case KEY_D:
                return PositionUtils.Direction.RIGHT;
        }

        return null;
    }
}
