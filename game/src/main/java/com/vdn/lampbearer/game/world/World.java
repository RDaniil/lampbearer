package com.vdn.lampbearer.game.world;

import com.vdn.lampbearer.entites.interfaces.AbstractEntity;
import com.vdn.lampbearer.game.Game;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.engine.Engine;
import com.vdn.lampbearer.game.engine.ScheduledEngine;
import com.vdn.lampbearer.game.world.block.GameBlock;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.game.GameArea;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.KeyboardEvent;

import java.util.Map;

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
        GameBlock block = fetchBlockAtOrElse(position3D,
                (pos) -> {
                    throw new IllegalArgumentException(String.format("Position %s does not contains any blocks", pos));
                });

        entity.setPosition(position3D);
        block.addEntity(entity);
        engine.addEntity(entity);
    }

    public boolean moveEntity(AbstractEntity entity, Position3D position) {
        var success = false;
        var oldBlock = fetchBlockAtOrNull(entity.getPosition());
        var newBlock = fetchBlockAtOrNull(position);

        if (oldBlock != null && newBlock != null) {
            success = true;
            oldBlock.removeEntity(entity);
            entity.setPosition(position);
            newBlock.addEntity(entity);
        }

        return success;
    }

    public void update(Screen screen, KeyboardEvent event, Game game) {
        engine.executeTurn(new GameContext(this, screen, event, game.getPlayer()));
    }
}
