package com.vdn.lampbearer.entites.item.projectile.revolver;

import com.vdn.lampbearer.attributes.projectile.PenetrationPowerAttr;
import com.vdn.lampbearer.attributes.projectile.ProjectileDamageAttr;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.light.CircleSparkLight;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.shape.LineFactory;

public class DefaultRevolverRound extends AbstractRevolverRound {

    public DefaultRevolverRound(Position3D position) {
        super(position);
        BlockType blockType = BlockType.DEFAULT_REVOLVER_ROUND;
        setTile(TileRepository.getTile(blockType));
        GameBlock gameBlock = GameBlockFactory.returnGameBlock(blockType);
        setName(gameBlock.getName());
        setDescription(gameBlock.getDescription());

        setPenetrationPower(new PenetrationPowerAttr(3));
        setProjectileDamage(new ProjectileDamageAttr(10));
    }


    @Override
    public void beforeLaunch(GameContext context, Position startPosition, Position targetPosition) {
        CircleSparkLight.createNow(context, startPosition, 6,
                TileColor.fromString("#FFbb33"), 1);
    }


    @Override
    protected void onPathEnd(GameContext context) {
        super.destroyProjectile(context);
    }


    @Override
    public boolean needToBeAnimated() {
        return isFlying;
    }
}
