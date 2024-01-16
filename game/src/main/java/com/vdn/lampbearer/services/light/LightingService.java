package com.vdn.lampbearer.services.light;

import com.vdn.lampbearer.constants.BlockLightingState;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.World;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.light.strategy.LightingStrategy;
import com.vdn.lampbearer.services.light.strategy.ShadowCastingLightingStrategy;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Position3D;

import java.util.HashMap;
import java.util.Map;

public class LightingService {

    private final Map<Position3D, GameBlock> worldBlocks;
    private final World world;
    private final LightingStrategy lightingStrategy;


    public LightingService(World world, Map<Position3D, GameBlock> worldBlocks) {
        this.worldBlocks = worldBlocks;
        this.world = world;
        this.lightingStrategy = new ShadowCastingLightingStrategy(
                world.getActualSize().to2DSize(), world.getBlocks());
    }


    public void updateLighting(GameContext context) {
        resetLightedBlocks();
        Player player = context.getPlayer();

        HashMap<Position, TileColor> positionToColorMap = lightingStrategy.lightBlocks(
                player.getFowLight());
        for (var posToColor : positionToColorMap.entrySet()) {
            updateLighting(posToColor.getKey(), posToColor.getValue());
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
            }
        }
    }
}
