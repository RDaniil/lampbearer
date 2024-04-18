package com.vdn.lampbearer.entites.item;

import com.vdn.lampbearer.action.actions.InspectItemAction;
import com.vdn.lampbearer.action.actions.inventory.PickUpItemAction;
import com.vdn.lampbearer.action.actions.items.FuelLanternAction;
import com.vdn.lampbearer.attributes.items.UsableAttr;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import org.hexworks.zircon.api.data.Position3D;

import java.util.List;

public class OilBottle extends AbstractItem {
    private final UsableAttr usableAttr;


    public OilBottle(Position3D position3D) {
        super(position3D);

        BlockType type = BlockType.OIL_BOTTLE;
        GameBlock block = GameBlockFactory.returnGameBlock(type);
        setTile(TileRepository.getTile(type));
        setName(block.getName());
        setDescription(block.getDescription());

        this.usableAttr = new UsableAttr(200);
        setAttributes(List.of(usableAttr));

        getActions().add(PickUpItemAction.getInstance());
        getActions().add(FuelLanternAction.getInstance());
        getActions().add(InspectItemAction.getInstance());
    }


    @Override
    public String getDescription() {
        String description = super.getDescription();
        return String.format("%s. %s", description, usableAttr.getStringPercentageLeft());
    }
}
