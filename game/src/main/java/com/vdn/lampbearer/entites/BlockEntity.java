package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.constants.BlockLightingState;
import com.vdn.lampbearer.game.world.block.GameBlock;
import org.hexworks.zircon.api.data.Position;

/**
 * Утилитарная сущность, нужна, чтобы хранить информацию о блоке в виде сущности
 */
public class BlockEntity extends AbstractEntity {
    public BlockEntity(Position position, GameBlock block) {
        setPosition(position.toPosition3D(0));

        if (block.getLightingState().equals(BlockLightingState.UNSEEN)) {
            setName("I can't see there");
            setDescription("");
        } else {
            setName("Block");
            setDescription("Block description");
        }
    }
}
