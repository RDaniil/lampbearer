package com.vdn.lampbearer.entites.item.projectile;

import com.vdn.lampbearer.action.actions.DropItemAction;
import com.vdn.lampbearer.action.actions.PickUpItemAction;
import com.vdn.lampbearer.action.actions.ThrowAction;
import com.vdn.lampbearer.attributes.projectile.PenetrationPowerAttr;
import com.vdn.lampbearer.attributes.projectile.ProjectileDamageAttr;
import com.vdn.lampbearer.entites.item.interfaces.Projectile;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.views.BlockTypes;
import com.vdn.lampbearer.views.TileRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
public class Stone extends Projectile {
    @Override
    public boolean needToBeAnimated() {
        return isFlying;
    }


    @Override
    protected void onPathEnd(GameContext context) {
        super.onPathEnd(context);
        this.getActions().add(PickUpItemAction.getInstance());
    }


    public Stone() {
        super();
        BlockTypes stoneType = BlockTypes.STONE;
        setTile(TileRepository.getTile(stoneType));
        GameBlock stoneBlock = GameBlockFactory.returnGameBlock(stoneType);
        setName(stoneBlock.getName());
        setDescription(stoneBlock.getDescription());

        setPenetrationPower(new PenetrationPowerAttr(0));
        setProjectileDamage(new ProjectileDamageAttr(10));

        getActions().add(ThrowAction.getInstance());
    }


    public static Stone createForInventory() {
        Stone stone = new Stone();
        stone.getActions().add(DropItemAction.getInstance());
        return stone;
    }


    @Override
    public String toString() {
        return "Stone{" +
                "penetrationPower=" + penetrationPower.getUsesLeft() +
                ", targetPosition=" + targetPosition +
                ", position=" + position +
                '}';
    }


    public static Stone createForWorld() {
        Stone stone = new Stone();
        stone.getActions().add(PickUpItemAction.getInstance());
        return stone;
    }
}
