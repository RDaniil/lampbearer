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
import org.hexworks.zircon.api.shape.LineFactory;

public class DefaultRevolverRound extends AbstractRevolverRound {

    public DefaultRevolverRound() {
        super();
        BlockTypes stoneType = BlockTypes.DEFAULT_REVOLVER_ROUND;
        setTile(TileRepository.getTile(stoneType));
        GameBlock stoneBlock = GameBlockFactory.returnGameBlock(stoneType);
        setName(stoneBlock.getName());
        setDescription(stoneBlock.getDescription());

        setPenetrationPower(new PenetrationPowerAttr(3));
        setProjectileDamage(new ProjectileDamageAttr(10));
    }


    @Override
    public void beforeLaunch(GameContext context, Position startPosition, Position targetPosition) {
        projectilePath = LineFactory.INSTANCE.buildLine(getPosition().to2DPosition(),
                getTargetPosition().to2DPosition()).iterator();
        //Сдвигаем позицию один раз, чтобы спавнить вспышку перед стреляющим
        projectilePath.next();
        CircleSparkLight.createNow(context, projectilePath.next(), 6,
                TileColor.fromString("#FFbb33"), 100);
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
