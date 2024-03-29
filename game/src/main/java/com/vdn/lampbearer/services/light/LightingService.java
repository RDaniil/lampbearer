package com.vdn.lampbearer.services.light;

import com.vdn.lampbearer.constants.BlockLightingState;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.game.world.World;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.ColorBlender;
import com.vdn.lampbearer.services.light.strategy.LightingStrategy;
import com.vdn.lampbearer.services.light.strategy.ShadowCastingLightingStrategy;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Position3D;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LightingService {

    private final Map<Position3D, GameBlock> worldBlocks;
    private final LightingStrategy lightingStrategy;
    /**
     * Мапка для хранения динамического света. Динамический свет может двигаться вместе с
     * какой-то сущностью.
     * Соответственно мапка сущнось-свет
     */
    private final HashMap<AbstractEntity, Set<Light>> entityToDynamicLight = new HashMap<>();

    private final HashSet<Light> staticLights = new HashSet<>();


    public LightingService(World world, Map<Position3D, GameBlock> worldBlocks) {
        this.worldBlocks = worldBlocks;
        this.lightingStrategy = new ShadowCastingLightingStrategy(
                world.getActualSize().to2DSize(), world.getBlocks());
    }


    public void updateLighting(Player player) {
        resetLightedBlocks();

        PlayerSight playerSight = player.getSight();
        HashMap<Position, TileColor> positionToColorMap = lightingStrategy
                .lightBlocks(playerSight);
        playerSight.reset(positionToColorMap.keySet());

        var lights = entityToDynamicLight.values().stream()
                .flatMap(Set::stream).collect(Collectors.toSet());
        lights.addAll(staticLights);

        for (Light light : lights) {
            positionToColorMap = lightingStrategy.lightBlocks(light);
            for (var posToColor : positionToColorMap.entrySet()) {
                var position = posToColor.getKey().toPosition3D(0);
                if (!playerSight.contains(position)) continue;

                GameBlock block = worldBlocks.get(position);
                if (block.getLightingState() == BlockLightingState.IN_LIGHT) {
                    updateLightningWithMixingColor(block, posToColor.getKey(), posToColor.getValue());
                } else {
                    updateLighting(posToColor.getKey(), posToColor.getValue());
                }
            }
        }
    }


    public void updateLightningWithMixingColor(GameBlock block,
                                               Position position,
                                               TileColor color) {
        updateLighting(
                position,
                ColorBlender.mixLightColors(color, block.getContent().getForegroundColor())
        );
    }


    public void addDynamicLight(AbstractEntity entity, Light dynamicLight) {
        if (!entityToDynamicLight.containsKey(entity)) {
            entityToDynamicLight.put(entity, new HashSet<>());
        }
        entityToDynamicLight.get(entity).add(dynamicLight);
    }


    public void removeDynamicLight(AbstractEntity entity, Light dynamicLight) {
        if (entityToDynamicLight.containsKey(entity)) {
            entityToDynamicLight.get(entity).remove(dynamicLight);
        }
    }


    public void removeDynamicLight(AbstractEntity entity) {
        if (entityToDynamicLight.containsKey(entity)) {
            entityToDynamicLight.get(entity).clear();
        }
    }


    public void addStaticLight(Light staticLight) {
        staticLights.add(staticLight);
    }


    public void removeStaticLight(Light staticLight) {
        staticLights.remove(staticLight);
    }


    public void removeLight(Light light) {
        if (!staticLights.remove(light)) {
            for (Set<Light> lightsByEntity : entityToDynamicLight.values()) {
                lightsByEntity.remove(light);
            }
        }
    }


    public AbstractEntity getEntityByLight(Light light) {
        for (Map.Entry<AbstractEntity, Set<Light>> entityToLights : entityToDynamicLight.entrySet()) {
            if (entityToLights.getValue().contains(light)) {
                return entityToLights.getKey();
            }
        }
        return null;
    }


    public boolean isEntityContainsLight(AbstractEntity entity) {
        return entityToDynamicLight.containsKey(entity);
    }


    public void moveDynamicLightWithEntity(AbstractEntity entity) {
        for (Light light : entityToDynamicLight.get(entity)) {
            light.setPosition(entity.getPosition().to2DPosition());
        }
    }


    private void updateLighting(Position position, TileColor color) {
        GameBlock block = worldBlocks.get(position.toPosition3D(0));
        block.setLightingState(BlockLightingState.IN_LIGHT);
        block.updateTileColor(color);
        block.updateLighting();
    }


    private void resetLightedBlocks() {
        for (GameBlock block : worldBlocks.values()) {
            if (block.getLightingState() == BlockLightingState.IN_LIGHT) {
                block.setLightingState(BlockLightingState.SEEN);
                block.updateContent();
                block.updateLighting();
            }
        }
    }
}
