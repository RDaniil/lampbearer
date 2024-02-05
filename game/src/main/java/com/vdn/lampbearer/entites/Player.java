package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.action.actions.AttackAction;
import com.vdn.lampbearer.attributes.*;
import com.vdn.lampbearer.attributes.occupation.StaticBlockOccupier;
import com.vdn.lampbearer.entites.behavior.player.PlayerBehavior;
import com.vdn.lampbearer.entites.behavior.player.PlayerBehaviorManager;
import com.vdn.lampbearer.entites.interfaces.Schedulable;
import com.vdn.lampbearer.entites.item.FirstAidKit;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.light.PlayerFOWSight;
import com.vdn.lampbearer.views.BlockTypes;
import com.vdn.lampbearer.views.TileRepository;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.uievent.KeyCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Player extends Actor<PlayerBehaviorManager> implements Schedulable {

    //TODO: По идее это должен быть какой-то дженерик список, либо поведения могут вызывать другие поведения
    private final PlayerBehaviorManager behaviorManager = new PlayerBehaviorManager();

    /**
     * Зрение игрока без фонарей в тумане войны
     */
    private final PlayerFOWSight fowLight;


    public PlayerFOWSight getFowLight() {
        fowLight.setPosition(getPosition().to2DPosition());
        return fowLight;
    }

    private final Map<KeyCode, Position3D> keyToSurroundingPositionMap = new HashMap<>();


    public Player() {
        setName("Lampbearer");
        setTile(TileRepository.getTile(BlockTypes.PLAYER));
        InventoryAttr inventoryAttr = new InventoryAttr();
        inventoryAttr.putItem(FirstAidKit.createForInventory());
        inventoryAttr.putItem(FirstAidKit.createForInventory());
        inventoryAttr.putItem(FirstAidKit.createForInventory());
        HealthAttr healthAttr = new HealthAttr(100);
        healthAttr.reduceHealth(40);

        setAttributes(List.of(
                healthAttr,
                new StrengthAttr(5),
                new SpeedAttr(5),
                new PerceptionAttr(5),
                StaticBlockOccupier.getInstance(),
                inventoryAttr
        ));
        setActions(List.of(AttackAction.getInstance()));
        fowLight = new PlayerFOWSight(Position.create(0, 0), 2,
                TileColor.fromString("#fafaed"));
    }


    @Override
    public boolean makeAction(GameContext context) {
        return behaviorManager.act(this, context);
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


    public void changeBehavior(PlayerBehavior playerTargetBehavior, GameContext context) {
        behaviorManager.changeBehavior(playerTargetBehavior, context);
    }
}
