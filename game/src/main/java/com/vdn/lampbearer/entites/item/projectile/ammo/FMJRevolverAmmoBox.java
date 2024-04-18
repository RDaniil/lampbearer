package com.vdn.lampbearer.entites.item.projectile.ammo;

import com.vdn.lampbearer.action.actions.inventory.PickUpItemAction;
import com.vdn.lampbearer.action.actions.items.LoadFirearmAction;
import com.vdn.lampbearer.attributes.inventory.ItemContainerAttr;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.entites.item.projectile.revolver.DefaultRevolverRound;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.DiceBuilder;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import org.hexworks.zircon.api.data.Position3D;

import java.util.List;

public class FMJRevolverAmmoBox extends AbstractItem {
    public FMJRevolverAmmoBox(Position3D position) {
        super(position);
        setTile(TileRepository.getTile(BlockType.DEFAULT_REVOLVER_ROUND_BOX));
        GameBlock gameBlock = GameBlockFactory.returnGameBlock(BlockType.DEFAULT_REVOLVER_ROUND_BOX);
        setName(gameBlock.getName());
        setDescription(gameBlock.getDescription());
        setAttributes(List.of(
                        ItemContainerAttr.createFilled(
                                new DiceBuilder().dice(2, 6).roll(),
                                new DefaultRevolverRound(),
                                DefaultRevolverRound.class)
                )
        );

        getActions().add(LoadFirearmAction.getInstance());
    }


    public static FMJRevolverAmmoBox createForWorld(Position3D position) {
        FMJRevolverAmmoBox ammoBox = new FMJRevolverAmmoBox(position);
        ammoBox.getActions().add(PickUpItemAction.getInstance());
        return ammoBox;
    }
}
