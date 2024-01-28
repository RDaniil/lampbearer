package com.vdn.lampbearer.entites.item;

import com.vdn.lampbearer.action.actions.LightLampAction;
import com.vdn.lampbearer.action.actions.PickUpLightSourceAction;
import com.vdn.lampbearer.attributes.LightSourceAttr;
import com.vdn.lampbearer.entites.interfaces.Updateable;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.services.light.CircleLight;
import com.vdn.lampbearer.views.BlockTypes;
import com.vdn.lampbearer.views.TileRepository;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position3D;

import java.util.List;

public class Lantern extends AbstractItem implements Updateable {
    public Lantern(Position3D position3D) {
        super();
        setPosition(position3D);
        setTile(TileRepository.getTile(BlockTypes.LANTERN));
        setName("Lantern");
        setAttributes(List.of(
                        new LightSourceAttr(new CircleLight(position3D, 6, TileColor.fromString(
                                "#cba731")))
                        //TODO: Аттрибут масла, в будущем будут батарейки, мб получится женерично их
                        // реализовать
//                new Fuel\oilAttr(1)
                )
        );

        getActions().add(PickUpLightSourceAction.getInstance());
        getActions().add(LightLampAction.getInstance());
    }


    @Override
    public void update(GameContext context) {

    }
}
