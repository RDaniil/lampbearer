package com.vdn.lampbearer.entites.objects;

import com.vdn.lampbearer.action.actions.DoorOpenAction;
import com.vdn.lampbearer.attributes.arrangement.Arrangement;
import com.vdn.lampbearer.attributes.arrangement.VerticalArrangement;
import com.vdn.lampbearer.attributes.occupation.DynamicBlockOccupier;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.views.BlockTypes;
import com.vdn.lampbearer.views.TileRepository;

import java.util.List;

public class Door extends AbstractEntity {

    public Door(Arrangement arrangement) {
        setName("Door");
        setTile(arrangement instanceof VerticalArrangement ?
                TileRepository.getTile(BlockTypes.V_CLOSED_DOOR) :
                TileRepository.getTile(BlockTypes.H_CLOSED_DOOR));
        setAttributes(List.of(DynamicBlockOccupier.getInstance(), arrangement));
        setActions(List.of(DoorOpenAction.getInstance()));
    }
}
