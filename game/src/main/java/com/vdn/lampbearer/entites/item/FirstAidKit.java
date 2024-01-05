package com.vdn.lampbearer.entites.item;

import com.vdn.lampbearer.attributes.items.HealingItemAttribute;
import com.vdn.lampbearer.services.DiceBuilder;
import com.vdn.lampbearer.views.TileRepository;

import java.util.List;

public class FirstAidKit extends AbstractItem {
    public FirstAidKit() {
        super();
        setTile(TileRepository.FIRST_AID_KIT);
        setName("First aid kit");
        setAttributes(List.of(
                new HealingItemAttribute(new DiceBuilder().dice(1, 6).plus(1).roll()))
        );
    }
}
