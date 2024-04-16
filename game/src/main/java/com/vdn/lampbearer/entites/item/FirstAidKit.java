package com.vdn.lampbearer.entites.item;

import com.vdn.lampbearer.action.actions.DropItemAction;
import com.vdn.lampbearer.action.actions.HealAction;
import com.vdn.lampbearer.action.actions.PickUpItemAction;
import com.vdn.lampbearer.attributes.UsableAttr;
import com.vdn.lampbearer.attributes.items.HealingItemAttribute;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.DiceBuilder;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import org.hexworks.zircon.api.data.Position3D;

import java.util.List;

public class FirstAidKit extends AbstractItem {

    private FirstAidKit(Position3D position) {
        super(position);

        BlockType type = BlockType.FIRST_AID_KIT;
        GameBlock block = GameBlockFactory.returnGameBlock(type);
        setTile(TileRepository.getTile(type));
        setName(block.getName());
        setDescription(block.getDescription());
        setAttributes(List.of(
                new HealingItemAttribute(new DiceBuilder().dice(2, 6).plus(1).roll()),
                new UsableAttr(1)
        ));

        getActions().add(HealAction.getInstance());
    }


    public static FirstAidKit createForInventory() {
        FirstAidKit firstAidKit = new FirstAidKit(Position3D.defaultPosition());
        firstAidKit.getActions().add(DropItemAction.getInstance());
        return firstAidKit;
    }


    public static FirstAidKit createForWorld(Position3D position3D) {
        FirstAidKit firstAidKit = new FirstAidKit(position3D);
        firstAidKit.getActions().add(PickUpItemAction.getInstance());
        return firstAidKit;
    }
}
