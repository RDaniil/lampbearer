package com.vdn.lampbearer.entites.item;

import com.vdn.lampbearer.action.actions.FuelLanternAction;
import com.vdn.lampbearer.action.actions.InspectItemAction;
import com.vdn.lampbearer.action.actions.PickUpItemAction;
import com.vdn.lampbearer.attributes.UsableAttr;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.views.BlockTypes;
import com.vdn.lampbearer.views.TileRepository;
import org.hexworks.zircon.api.data.Position3D;

import java.util.List;

public class OilBottle extends AbstractItem {
    private final UsableAttr usableAttr;


    public OilBottle(Position3D position3D) {
        super();
        setPosition(position3D);
        setTile(TileRepository.getTile(BlockTypes.OIL_BOTTLE));
        setName(GameBlockFactory.returnGameBlock(BlockTypes.OIL_BOTTLE).getName());
        this.usableAttr = new UsableAttr(200);
        setAttributes(List.of(usableAttr));

        getActions().add(PickUpItemAction.getInstance());
        getActions().add(FuelLanternAction.getInstance());
        getActions().add(InspectItemAction.getInstance());
    }


    @Override
    public String getDescription() {
        return String.format("%s. %s",
                GameBlockFactory.returnGameBlock(BlockTypes.OIL_BOTTLE).getDescription(),
                usableAttr.getStringPercentageLeft());
    }
}
