package com.vdn.lampbearer.entites.objects;

import com.vdn.lampbearer.action.actions.interaction.DoorOpenAction;
import com.vdn.lampbearer.action.actions.interaction.LampbearerDoorOpenAction;
import com.vdn.lampbearer.attributes.arrangement.Arrangement;
import com.vdn.lampbearer.attributes.arrangement.VerticalArrangement;
import com.vdn.lampbearer.attributes.occupation.DynamicBlockOccupier;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import org.hexworks.zircon.api.data.Position3D;

import java.util.List;

public class LampbearerHouseDoor extends Door {

    public LampbearerHouseDoor(Position3D position3D) {
        super(position3D);

        BlockType blockType = BlockType.OPENED_DOOR_LAMBEARER_HOUSE;
        setName(GameBlockFactory.returnGameBlock(blockType).getName());
        setDescription(GameBlockFactory.returnGameBlock(blockType).getDescription());
        setTile(TileRepository.getTile(blockType));
        setActions(List.of(LampbearerDoorOpenAction.getInstance()));
    }


    public LampbearerHouseDoor(Position3D position3D, Arrangement arrangement) {
        super(position3D);

        BlockType blockType = arrangement instanceof VerticalArrangement ?
                BlockType.V_CLOSED_DOOR_LAMBEARER_HOUSE : BlockType.H_CLOSED_DOOR_LAMBEARER_HOUSE;

        setName(GameBlockFactory.returnGameBlock(blockType).getName());
        setDescription(GameBlockFactory.returnGameBlock(blockType).getDescription());
        setTile(TileRepository.getTile(blockType));
        setAttributes(List.of(DynamicBlockOccupier.getInstance(), arrangement));
        setActions(List.of(LampbearerDoorOpenAction.getInstance()));
    }
}
