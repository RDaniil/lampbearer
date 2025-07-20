package com.vdn.lampbearer.entites.item.projectile.revolver;

import com.vdn.lampbearer.action.actions.LoadOneInFirearmAction;
import com.vdn.lampbearer.action.actions.ShootFirearmAction;
import com.vdn.lampbearer.action.actions.inventory.PickUpItemAction;
import com.vdn.lampbearer.attributes.LightSourceAttr;
import com.vdn.lampbearer.entites.item.Lantern;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.light.CircleLight;
import com.vdn.lampbearer.services.light.CircleSparkLight;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Position3D;

@Slf4j
public class SignalRevolverRound extends AbstractRevolverRound {

    /**
     * Время, которое огонек будет "висеть" в воздухе
     */
    private static final int LINGER_TIME = 60;

    /**
     * Процент пройденного пути, начиная с которого ракета замедлится
     */
    private static final double PATH_SLOWING_PERCENTAGE = 0.7;

    /**
     * Время, начиная с которого огонек начнет тухнуть (уменьшать интенсивность свечения)
     */
    public static final int LINGER_TIME_LIGHT_THRESHOLD = 12;

    /**
     * Процент уменьшения интенсивности свечения, когда огонек начинает тухнуть
     */
    public static final double LINGER_DARKENING_PERCENTAGE = 0.2;

    /**
     * Скорость ракеты когда она начала замедляться
     */
    public static final int SLOWED_SPEED = 2;
    private int lingerTimeLeft;
    private boolean isLingering;
    private CircleLight signalLight;


    public SignalRevolverRound(Position3D position) {
        super(position);
        BlockType blockType = BlockType.SIGNAL_REVOLVER_ROUND;
        setTile(TileRepository.getTile(blockType));
        GameBlock gameBlock = GameBlockFactory.returnGameBlock(blockType);
        setName(gameBlock.getName());
        setDescription(gameBlock.getDescription());
        getActions().add(ShootFirearmAction.getInstance());
        getActions().add(LoadOneInFirearmAction.getInstance());
        lingerTimeLeft = LINGER_TIME;

        signalLight = new CircleLight(position, 8, TileColor.fromString("#FF2211"));
    }


    public static SignalRevolverRound createForWorld(Position3D position) {
        SignalRevolverRound signalRevolverRound = new SignalRevolverRound(position);
        signalRevolverRound.getActions().add(PickUpItemAction.getInstance());
        return signalRevolverRound;
    }


    @Override
    public int getTime() {
        if (getPathPercentage() >= PATH_SLOWING_PERCENTAGE) {
            return SLOWED_SPEED;
        }
        return isLingering ? 1 : 0;
    }

    @Override
    protected void onPathStart(GameContext context, Position startPosition) {
        signalLight.setPosition(startPosition);
        context.getWorld().addDynamicLight(this, signalLight);
    }


    @Override
    public void beforeLaunch(GameContext context, Position startPosition, Position targetPosition) {
        CircleSparkLight.createNow(context, startPosition, 6,
                TileColor.fromString("#FF6633"), 1);
        onPathStart(context, startPosition);
    }


    @Override
    protected void onCollision(GameContext context) {
        //do nothing
    }


    @Override
    public void update(GameContext context) {
        if (isLingering) {
            updateLingering(context);
        } else {
            super.update(context);
        }

        context.getWorld().updateLighting();
    }


    private void updateLingering(GameContext context) {
        lingerTimeLeft--;
        if (lingerTimeLeft == 0) {
            super.destroyProjectile(context);
        }
        changeLightIntensity();
    }


    private void changeLightIntensity() {
        if (lingerTimeLeft <= SignalRevolverRound.LINGER_TIME_LIGHT_THRESHOLD) {
            var newRadius = signalLight.getRadius() - 1;
            signalLight.setRadius(newRadius > 1 ? newRadius : 2);
            signalLight.getColor().darkenByPercent(LINGER_DARKENING_PERCENTAGE);
        }
    }


    @Override
    protected void onPathEnd(GameContext context) {
        isLingering = true;
    }


    @Override
    public boolean needToBeAnimated() {
        return isFlying;
    }


    @Override
    public SignalRevolverRound clone() {
        SignalRevolverRound clone = (SignalRevolverRound) super.clone();
        clone.signalLight = (CircleLight) signalLight.clone();
        return clone;
    }
}
