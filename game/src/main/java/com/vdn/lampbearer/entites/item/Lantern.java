package com.vdn.lampbearer.entites.item;

import com.vdn.lampbearer.action.actions.InspectItemAction;
import com.vdn.lampbearer.action.actions.inventory.PickUpLightSourceAction;
import com.vdn.lampbearer.action.actions.items.LightLampAction;
import com.vdn.lampbearer.action.reactions.items.PutOutLampReaction;
import com.vdn.lampbearer.attributes.LightSourceAttr;
import com.vdn.lampbearer.attributes.items.FueledByOilAttr;
import com.vdn.lampbearer.attributes.items.UsableAttr;
import com.vdn.lampbearer.entites.interfaces.Updatable;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.light.CircleLight;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position3D;

import java.util.List;

@Slf4j
public class Lantern extends AbstractItem implements Updatable {

    private static final int MAX_LIGHT_RADIUS = 6;
    private static final float START_FADING_PERCENTAGE = 20;

    private final UsableAttr usableAttr;

    @Setter(AccessLevel.NONE)
    private LightSourceAttr lightSource;


    @Override
    public int getTime() {
        return 10;
    }


    @Override
    public boolean needUpdate() {
        return lightSource.isOn();
    }


    public Lantern(Position3D position3D) {
        super(position3D);

        BlockType type = BlockType.LANTERN;
        GameBlock block = GameBlockFactory.returnGameBlock(type);
        setTile(TileRepository.getTile(type));
        setName(block.getName());
        setDescription(block.getDescription());

        usableAttr = new UsableAttr(100, 70);
        lightSource = new LightSourceAttr(
                new CircleLight(position3D, MAX_LIGHT_RADIUS, TileColor.fromString("#cba731"))
        );

        setAttributes(List.of(
                this.lightSource,
                FueledByOilAttr.getInstance(),
                usableAttr
        ));

        getActions().add(PickUpLightSourceAction.getInstance());
        getActions().add(LightLampAction.getInstance());
        getActions().add(InspectItemAction.getInstance());
    }


    @Override
    public String getDescription() {
        String description = super.getDescription();
        return String.format("%s. %s", description, usableAttr.getStringPercentageLeft());
    }


    @Override
    public void update(GameContext context) {
        if (!lightSource.isOn()) {
            return;
        }

        if (usableAttr.useOnce() == 0) {
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
    public Lantern clone() {
        Lantern clone = (Lantern) super.clone();
        clone.lightSource = lightSource.clone();
        clone.removeAttribute(LightSourceAttr.class);
        clone.getAttributes().add(clone.lightSource);
        return clone;
    }
}
