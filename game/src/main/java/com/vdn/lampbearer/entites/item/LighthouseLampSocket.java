package com.vdn.lampbearer.entites.item;

import com.vdn.lampbearer.action.actions.inventory.PickUpItemAction;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import org.hexworks.zircon.api.data.Position3D;

public class LighthouseLampSocket extends AbstractItem {

    private LighthouseLampSocket(Position3D position) {
        super(position);

        BlockType type = BlockType.LIGHTHOUSE_LAMP_SOCKET;
        GameBlock block = GameBlockFactory.returnGameBlock(type);
        setTile(TileRepository.getTile(type));
        setName(block.getName());
        setDescription(block.getDescription());
    }


    public static LighthouseLampSocket createForWorld(Position3D position3D) {
        LighthouseLampSocket item = new LighthouseLampSocket(position3D);
        item.getActions().add(PickUpItemAction.getInstance());
        return item;
    }
}
