package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.game.world.block.GameBlock;
import org.hexworks.zircon.api.data.Position;

/**
 * Утилитарная сущность, нужна, чтобы хранить информацию о блоке в виде сущности
 */
public class BlockEntity extends AbstractEntity {
    public BlockEntity(Position position, GameBlock block) {
        super(position.toPosition3D(0));
        setName(block.getName());
        setDescription(block.getDescription());
    }


    public BlockEntity(Position position, String blockEntityName, String blockEntityDescription) {
        super(position.toPosition3D(0));
        setName(blockEntityName);
        setDescription(blockEntityDescription);
    }
}
