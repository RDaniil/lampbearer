package com.vdn.lampbearer.entites.item;

import com.vdn.lampbearer.action.HealAction;
import com.vdn.lampbearer.action.interaction.DropItemAction;
import com.vdn.lampbearer.action.interaction.PickUpItemAction;
import com.vdn.lampbearer.attributes.UsableAttr;
import com.vdn.lampbearer.attributes.items.HealingItemAttribute;
import com.vdn.lampbearer.services.DiceBuilder;
import com.vdn.lampbearer.views.BlockTypes;
import com.vdn.lampbearer.views.TileRepository;

import java.util.List;

public class FirstAidKit extends AbstractItem {
    public FirstAidKit() {
        super();
        setTile(TileRepository.getTile(BlockTypes.FIRST_AID_KIT));
        setName("First aid kit");
        setAttributes(List.of(
                        new HealingItemAttribute(new DiceBuilder().dice(2, 6).plus(1).roll()),
                        new UsableAttr(1)
                )
        );

        getActions().add(HealAction.getInstance());
    }


    public static FirstAidKit createForInventory() {
        FirstAidKit firstAidKit = new FirstAidKit();
        firstAidKit.getActions().add(DropItemAction.getInstance());
        return firstAidKit;
    }


    public static FirstAidKit createForWorld() {
        FirstAidKit firstAidKit = new FirstAidKit();
        firstAidKit.getActions().add(PickUpItemAction.getInstance());
        return firstAidKit;
    }
}
