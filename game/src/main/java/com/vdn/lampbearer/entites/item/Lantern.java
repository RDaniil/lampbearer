package com.vdn.lampbearer.entites.item;

import com.vdn.lampbearer.action.actions.HealAction;
import com.vdn.lampbearer.attributes.LightSourceAttr;
import com.vdn.lampbearer.entites.interfaces.Updateable;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.services.light.CircleLight;
import com.vdn.lampbearer.views.TileRepository;
import org.hexworks.zircon.api.color.TileColor;

import java.util.List;

public class Lantern extends AbstractItem implements Updateable {
    public Lantern() {
        super();
        setTile(TileRepository.FIRST_AID_KIT);
        setName("Lantern");
        setAttributes(List.of(
                        new LightSourceAttr(new CircleLight(6, TileColor.fromString("#cba731")))
                        //TODO: Аттрибут масла, в будущем будут батарейки, мб получится женерично их
                        // реализовать
//                new Fuel\oilAttr(1)
                )
        );

        getActions().add(HealAction.getInstance());
    }


    @Override
    public void update(GameContext context) {

    }
}
