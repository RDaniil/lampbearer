package com.vdn.lampbearer.entites.item;

import com.vdn.lampbearer.action.actions.inventory.PickUpItemAction;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import org.hexworks.zircon.api.data.Position3D;

public class BrokenLighthouseKey extends AbstractItem {

    private BrokenLighthouseKey(Position3D position) {
        super(position);

        BlockType type = BlockType.BROKEN_LIGHTHOUSE_KEY;
        GameBlock block = GameBlockFactory.returnGameBlock(type);
        setTile(TileRepository.getTile(type));
        setName(block.getName());
        setDescription(block.getDescription());
    }


    public static BrokenLighthouseKey createForWorld(Position3D position3D) {
        return new BrokenLighthouseKey(position3D);
    }
}
