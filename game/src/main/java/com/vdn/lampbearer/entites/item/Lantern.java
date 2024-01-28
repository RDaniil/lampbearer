package com.vdn.lampbearer.entites.item;

import com.vdn.lampbearer.action.actions.InspectItemAction;
import com.vdn.lampbearer.action.actions.LightLampAction;
import com.vdn.lampbearer.action.actions.PickUpLightSourceAction;
import com.vdn.lampbearer.action.reactions.PutOutLampReaction;
import com.vdn.lampbearer.attributes.LightSourceAttr;
import com.vdn.lampbearer.attributes.UsableAttr;
import com.vdn.lampbearer.attributes.items.FueledByOilAttr;
import com.vdn.lampbearer.entites.interfaces.Schedulable;
import com.vdn.lampbearer.entites.interfaces.Updateable;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.services.light.CircleLight;
import com.vdn.lampbearer.views.BlockTypes;
import com.vdn.lampbearer.views.TileRepository;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position3D;

import java.util.List;

@Slf4j
public class Lantern extends AbstractItem implements Updateable, Schedulable {
    private final UsableAttr usableAttr;
    private final LightSourceAttr lightSource;


    @Override
    public int getTime() {
        return 1;
    }

    public Lantern(Position3D position3D) {
        super();
        setPosition(position3D);
        setTile(TileRepository.getTile(BlockTypes.LANTERN));
        setName("Lantern");

        usableAttr = new UsableAttr(100, 70);
        lightSource = new LightSourceAttr(new CircleLight(position3D, 6, TileColor.fromString(
                "#cba731")));

        setAttributes(List.of(
                this.lightSource,
                FueledByOilAttr.getInstance(),
                usableAttr)
        );

        getActions().add(PickUpLightSourceAction.getInstance());
        getActions().add(LightLampAction.getInstance());
        getActions().add(InspectItemAction.getInstance());
    }


    @Override
    public String getDescription() {
        return usableAttr.getStringPercentageLeft();
    }


    @Override
    public void update(GameContext context) {
        if (!lightSource.isOn()) {
            return;
        }

        if (usableAttr.useOnce() == 0) {
            new PutOutLampReaction().execute(null, this, context);
        }
    }
}
