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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class LightingService {

    private final Map<Position3D, GameBlock> worldBlocks;
    private final LightingStrategy lightingStrategy;
    /**
     * Мапка для хранения динамического света. Динамический свет может двигаться вместе с
     * какой-то сущностью.
     * Соответственно мапка сущнось-свет
     */
    private final ConcurrentHashMap<AbstractEntity, Set<Light>> entityToDynamicLight = new ConcurrentHashMap<>();

    private final CopyOnWriteArraySet<Light> staticLights = new CopyOnWriteArraySet<>();
    
    // Optimization caches
    private final Map<Light, HashMap<Position, TileColor>> staticLightCache = new ConcurrentHashMap<>();
    private final Set<Position3D> currentlyLitBlocks = new HashSet<>();
    private final Set<Position3D> previouslyLitBlocks = new HashSet<>();
    private final Map<AbstractEntity, Position> entityPreviousPositions = new HashMap<>();
    private Position3D playerPreviousPosition;


    public LightingService(World world, Map<Position3D, GameBlock> worldBlocks) {
        this.worldBlocks = worldBlocks;
        this.lightingStrategy = new ShadowCastingLightingStrategy(
                world.getActualSize().to2DSize(), world.getBlocks());
    }


    public void updateLighting(Player player) {
        Position3D currentPlayerPosition = player.getPosition();
        boolean playerMoved = !currentPlayerPosition.equals(playerPreviousPosition);
        
        // Check if any dynamic light entities moved
        Set<AbstractEntity> movedEntities = getMovedEntities();
        
        // Only do expensive operations if something changed
        if (playerMoved || !movedEntities.isEmpty()) {
            // Store previous lit blocks for cleanup
            previouslyLitBlocks.clear();
            previouslyLitBlocks.addAll(currentlyLitBlocks);
            currentlyLitBlocks.clear();
            
            // Update player sight
            PlayerSight playerSight = player.getSight();
            HashMap<Position, TileColor> positionToColorMap = lightingStrategy
                    .lightBlocks(playerSight);
            playerSight.reset(positionToColorMap.keySet());
            
            // Process static lights (cached)
            processStaticLights(playerSight);
            
            // Process dynamic lights (only recalculate for moved entities)
            processDynamicLights(playerSight, movedEntities);
            
            // Clean up blocks that are no longer lit
            cleanupUnlitBlocks();
            
            // Update tracking
            playerPreviousPosition = currentPlayerPosition;
            updateEntityPositions();
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
        entityToDynamicLight.computeIfAbsent(entity, k -> new CopyOnWriteArraySet<>())
                           .add(dynamicLight);
    }


    public void removeDynamicLight(AbstractEntity entity, Light dynamicLight) {
        Set<Light> lights = entityToDynamicLight.get(entity);
        if (lights != null) {
            lights.remove(dynamicLight);
        }
    }


    public void removeDynamicLight(AbstractEntity entity) {
        Set<Light> lights = entityToDynamicLight.get(entity);
        if (lights != null) {
            lights.clear();
        }
    }


    public void addStaticLight(Light staticLight) {
        staticLights.add(staticLight);
    }


    public void removeStaticLight(Light staticLight) {
        staticLights.remove(staticLight);
        staticLightCache.remove(staticLight);
    }


    public void removeLight(Light light) {
        if (!staticLights.remove(light)) {
            for (Set<Light> lightsByEntity : entityToDynamicLight.values()) {
                lightsByEntity.remove(light);
            }
        } else {
            staticLightCache.remove(light);
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
        Set<Light> lights = entityToDynamicLight.get(entity);
        if (lights != null) {
            for (Light light : lights) {
                light.setPosition(entity.getPosition().to2DPosition());
            }
        }
    }


    private void updateLighting(Position position, TileColor color) {
        GameBlock block = worldBlocks.get(position.toPosition3D(0));
        block.setLightingState(BlockLightingState.IN_LIGHT);
        block.updateTileColor(color);
        block.updateLighting();
    }


    private Set<AbstractEntity> getMovedEntities() {
        Set<AbstractEntity> movedEntities = new HashSet<>();
        for (AbstractEntity entity : entityToDynamicLight.keySet()) {
            Position currentPos = entity.getPosition().to2DPosition();
            Position previousPos = entityPreviousPositions.get(entity);
            if (!currentPos.equals(previousPos)) {
                movedEntities.add(entity);
            }
        }
        return movedEntities;
    }
    
    private void processStaticLights(PlayerSight playerSight) {
        for (Light light : staticLights) {
            HashMap<Position, TileColor> lightMap = staticLightCache.computeIfAbsent(light, 
                lightingStrategy::lightBlocks);
            
            for (var posToColor : lightMap.entrySet()) {
                var position = posToColor.getKey().toPosition3D(0);
                if (!playerSight.contains(position)) continue;
                
                currentlyLitBlocks.add(position);
                GameBlock block = worldBlocks.get(position);
                if (block.getLightingState() == BlockLightingState.IN_LIGHT) {
                    updateLightningWithMixingColor(block, posToColor.getKey(), posToColor.getValue());
                } else {
                    updateLighting(posToColor.getKey(), posToColor.getValue());
                }
            }
        }
    }
    
    private void processDynamicLights(PlayerSight playerSight, Set<AbstractEntity> movedEntities) {
        var dynamicLights = entityToDynamicLight.values().stream()
                .flatMap(Set::stream).collect(Collectors.toSet());
        
        for (Light light : dynamicLights) {
            if (shouldRecalculateDynamicLight(light, movedEntities)) {
                applyDynamicLight(light, playerSight);
            }
        }
    }
    
    private boolean shouldRecalculateDynamicLight(Light light, Set<AbstractEntity> movedEntities) {
        AbstractEntity entity = getEntityByLight(light);
        return entity == null || movedEntities.contains(entity) || !entityPreviousPositions.containsKey(entity);
    }
    
    private void applyDynamicLight(Light light, PlayerSight playerSight) {
        HashMap<Position, TileColor> positionToColorMap = lightingStrategy.lightBlocks(light);
        
        for (var posToColor : positionToColorMap.entrySet()) {
            var position = posToColor.getKey().toPosition3D(0);
            if (!playerSight.contains(position)) continue;
            
            currentlyLitBlocks.add(position);
            GameBlock block = worldBlocks.get(position);
            if (block.getLightingState() == BlockLightingState.IN_LIGHT) {
                updateLightningWithMixingColor(block, posToColor.getKey(), posToColor.getValue());
            } else {
                updateLighting(posToColor.getKey(), posToColor.getValue());
            }
        }
    }
    
    private void cleanupUnlitBlocks() {
        for (Position3D position : previouslyLitBlocks) {
            if (!currentlyLitBlocks.contains(position)) {
                GameBlock block = worldBlocks.get(position);
                if (block.getLightingState() == BlockLightingState.IN_LIGHT) {
                    block.setLightingState(BlockLightingState.SEEN);
                    block.updateContent();
                    block.updateLighting();
                }
            }
        }
    }
    
    private void updateEntityPositions() {
        for (AbstractEntity entity : entityToDynamicLight.keySet()) {
            entityPreviousPositions.put(entity, entity.getPosition().to2DPosition());
        }
    }
}
