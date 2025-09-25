package com.vdn.lampbearer.entites.item;

import com.vdn.lampbearer.action.actions.InspectItemAction;
import com.vdn.lampbearer.action.actions.inventory.PickUpLightSourceAction;
import com.vdn.lampbearer.action.actions.items.LightLampAction;
import com.vdn.lampbearer.action.actions.items.ThrowAction;
import com.vdn.lampbearer.action.reactions.items.PutOutLampReaction;
import com.vdn.lampbearer.attributes.LightSourceAttr;
import com.vdn.lampbearer.attributes.creature.HealthAttr;
import com.vdn.lampbearer.attributes.items.FueledByOilAttr;
import com.vdn.lampbearer.attributes.items.UsableAttr;
import com.vdn.lampbearer.attributes.occupation.BlockOccupier;
import com.vdn.lampbearer.attributes.projectile.PenetrationPowerAttr;
import com.vdn.lampbearer.attributes.projectile.ProjectileDamageAttr;
import com.vdn.lampbearer.constants.GameOverResult;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.entites.item.interfaces.Projectile;
import com.vdn.lampbearer.exception.GameOverException;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.light.CircleLight;
import com.vdn.lampbearer.services.light.CircleSparkLight;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.component.Component;
import org.hexworks.zircon.api.component.HBox;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Position3D;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hexworks.zircon.api.ComponentDecorations.noDecoration;

@Slf4j
public class Lantern extends Projectile {

    private static final int MAX_LIGHT_RADIUS = 6;
    private static final float START_FADING_PERCENTAGE = 20;

    private final UsableAttr usableAttr;

    @Setter(AccessLevel.NONE)
    @Getter
    private LightSourceAttr lightSource;


    @Override
    public Component toComponent() {
        HBox component = (HBox) super.toComponent();

        if(lightSource.isOn()) {
            var status = Components.label()
                    .withDecorations(noDecoration())
                    .withText("(On)")
                    .build();

            component.addComponent(status);
        }
        return component;
    }

    @Override
    public int getTime() {
        return isFlying ? 0 : 10;
    }


    @Override
    public boolean needUpdate() {
        return isFlying || lightSource.isOn();
    }


    public Lantern(Position3D position3D) {
        super(position3D);

        BlockType type = BlockType.LANTERN;
        GameBlock block = GameBlockFactory.returnGameBlock(type);
        setTile(TileRepository.getTile(type));
        setName(block.getName());
        setDescription(block.getDescription());

        usableAttr = new UsableAttr(60, 60);
        lightSource = new LightSourceAttr(
                new CircleLight(position3D, MAX_LIGHT_RADIUS, TileColor.fromString("#cba731"))
        );

        setPenetrationPower(new PenetrationPowerAttr(1));
        setProjectileDamage(new ProjectileDamageAttr(15));

        setAttributes(List.of(
                this.lightSource,
                FueledByOilAttr.getInstance(),
                usableAttr
        ));

        getActions().add(PickUpLightSourceAction.getInstance());
        getActions().add(LightLampAction.getInstance());
        getActions().add(InspectItemAction.getInstance());
        getActions().add(ThrowAction.getInstance());
    }


    @Override
    public String getDescription() {
        String description = super.getDescription();
        return String.format("%s. %s", description, usableAttr.getStringPercentageLeft());
    }

    public String getFuelLeft(){
        return usableAttr.getStringPercentageLeft();
    }

    public int getFuelLeftNumber(){
        return usableAttr.getPercentageLeft();
    }

    @Override
    protected void onPathStart(GameContext context, Position startPosition) {
        context.getWorld().removeDynamicLight(context.getPlayer(), lightSource.getLight());
        context.getWorld().addDynamicLight(this, lightSource.getLight());
    }

    @Override
    public void update(GameContext context) {
        if (isFlying) {
            super.update(context);
            return;
        }

        if (!lightSource.isOn()) {
            return;
        }
        context.getWorld().updateLighting();
        if (usableAttr.useOnce() == 0) {
            context.getLogArea().addParagraph("Lantern ran out of oil!", false, 0);
            new PutOutLampReaction().execute(null, this, context);
            return;
        }

        int percentageLeft = usableAttr.getPercentageLeft();
        if (percentageLeft > START_FADING_PERCENTAGE) {
            lightSource.getLight().setRadius(MAX_LIGHT_RADIUS);
            return;
        }

        float newRadius = percentageLeft * (MAX_LIGHT_RADIUS - 1) / START_FADING_PERCENTAGE;
        lightSource.getLight().setRadius((int) (Math.max(newRadius, 1)));
    }

    @Override
    protected void onCollision(GameContext context) {
        if (lightSource.isOn()) {
            explode(context);
        } else {
            super.onCollision(context);
        }
    }

    @Override
    protected void onPathEnd(GameContext context) {
        if (lightSource.isOn()) {
            explode(context);
        } else {
            super.onPathEnd(context);
        }
    }

    private void explode(GameContext context) {
        final int explosionRadius = 3;
        final int explosionDamage = 25;
        
        Position centerPos = getPosition().to2DPosition();
        
        CircleSparkLight.createNow(context, centerPos, explosionRadius,
                TileColor.fromString("#FF8833"), 6);


        context.getLogArea().addInlineComponent("!BOOM!",
                TileColor.fromString("#222222"),
                TileColor.fromString("#FF8833"));
        context.getLogArea().commitInlineElements();

        context.getLogArea().addInlineText(
                String.format("%s explodes!", getName())
        );

        context.getLogArea().commitInlineElements();
        List<AbstractEntity> entitiesToDamage = getEntitiesToDamage(context, explosionRadius, centerPos);

        for (AbstractEntity entity : entitiesToDamage) {
            damageEntity(entity, explosionDamage, context);
        }

        destroyProjectile(context);
        context.getWorld().removeDynamicLight(lightSource.getLight());
    }

    private List<AbstractEntity> getEntitiesToDamage(GameContext context, int explosionRadius, Position centerPos) {
        List<AbstractEntity> entitiesToDamage = new java.util.ArrayList<>();

        for (int dx = -explosionRadius; dx <= explosionRadius; dx++) {
            for (int dy = -explosionRadius; dy <= explosionRadius; dy++) {
                Position targetPos = Position.create(centerPos.getX() + dx, centerPos.getY() + dy);
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance <= explosionRadius) {
                    Collection<AbstractEntity> entitiesAt = context.getWorld().getEntitiesAt(targetPos);
                    for (AbstractEntity entity : entitiesAt) {
                        if (entity.hasAttribute(BlockOccupier.class) && !entity.equals(this)) {
                            entitiesToDamage.add(entity);
                        }
                    }
                }
            }
        }
        return entitiesToDamage;
    }

    private void damageEntity(AbstractEntity target, int damage, GameContext context) {
        Optional<HealthAttr> healthAttr = target.findAttribute(HealthAttr.class);
        if (healthAttr.isEmpty()) {
            return;
        }

        int remainingHealth = healthAttr.get().reduceHealth(damage);
        context.getScoreService().addDamageDealt(damage);
        if(target instanceof Player){
            context.getScoreService().addDamageReceived(damage);
        }
        context.getLogArea().addInlineComponent(target.getName(), target.getTile().getForegroundColor());
        context.getLogArea().addInlineText(" takes ");
        context.getLogArea().addInlineText(String.format(
                " %d explosion damage",
                damage
        ));
        context.getLogArea().commitInlineElements();

        if (remainingHealth <= 0) {
            context.getLogArea().addInlineComponent(target.getName(), target.getTile().getForegroundColor());
            context.getLogArea().addInlineText(" dies from the explosion!");
            context.getLogArea().commitInlineElements();

            if(target instanceof Player){
                throw new GameOverException(context, GameOverResult.PLAYER_DEAD);
            }
            context.getWorld().deleteEntity(target, target.getPosition());
        }
    }

    @Override
    public boolean needToBeAnimated() {
        return isFlying;
    }


    @Override
    public Lantern clone() {
        Lantern clone = (Lantern) super.clone();
        clone.lightSource = lightSource.clone();
        clone.removeAttribute(LightSourceAttr.class);
        clone.getAttributes().add(clone.lightSource);
        return clone;
    }
}
