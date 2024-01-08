package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.action.AttackAction;
import com.vdn.lampbearer.attributes.HealthAttr;
import com.vdn.lampbearer.attributes.InventoryAttr;
import com.vdn.lampbearer.attributes.SpeedAttr;
import com.vdn.lampbearer.attributes.StrengthAttr;
import com.vdn.lampbearer.attributes.occupation.StaticBlockOccupier;
import com.vdn.lampbearer.entites.behavior.player.PlayerBehavior;
import com.vdn.lampbearer.entites.item.FirstAidKit;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.views.TileRepository;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.uievent.KeyCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Player extends Actor<PlayerBehavior> implements Schedulable {

    //TODO: По идее это должен быть какой-то дженерик список, либо поведения могут вызывать другие поведения
    private final PlayerBehavior behavior = new PlayerBehavior();

    private final Map<KeyCode, Position3D> keyToSurroundingPositionMap = new HashMap<>();


    public Player() {
        setName("Lampbearer");
        setTile(TileRepository.PLAYER);
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
                StaticBlockOccupier.getInstance(),
                inventoryAttr
        ));
        setActions(List.of(AttackAction.getInstance()));
    }


    @Override
    public boolean makeAction(GameContext context) {
        return behavior.act(this, context);
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
}
