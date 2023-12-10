package com.vdn.lampbearer.game;

import com.vdn.lampbearer.entites.interfaces.AbstractEntity;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.game.GameArea;

import java.util.Map;

/**
 * Содержит всю карту из блоков
 */
public class World extends WorldDelegate implements GameArea<Tile, GameBlock> {

    public World(Size3D visibleSize, Size3D actualSize, Map<Position3D, GameBlock> startingBlocks) {
        super(visibleSize, actualSize, startingBlocks);
    }

    public void addEntity(AbstractEntity entity, Position3D position3D) {
        GameBlock block = fetchBlockAtOrElse(position3D,
                (pos) -> {
                    throw new IllegalArgumentException(String.format("Position %s does not contains any blocks", pos));
                });

        entity.setPosition(position3D);
        block.addEntity(entity);
    }
}
