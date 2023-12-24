package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.action.interaction.DoorOpeningAction;
import com.vdn.lampbearer.attributes.BlockOccupier;
import com.vdn.lampbearer.attributes.arrangement.Arrangement;
import com.vdn.lampbearer.attributes.arrangement.VerticalArrangement;
import com.vdn.lampbearer.views.TileRepository;

import java.util.List;

public class Door extends AbstractEntity {

    public Door(Arrangement arrangement) {
        setName("Door");
        setTile(arrangement instanceof VerticalArrangement ?
                TileRepository.V_CLOSED_DOOR : TileRepository.H_CLOSED_DOOR);
        setAttributes(List.of(BlockOccupier.getInstance(), arrangement));
        setActions(List.of(DoorOpeningAction.getInstance()));
    }
}
