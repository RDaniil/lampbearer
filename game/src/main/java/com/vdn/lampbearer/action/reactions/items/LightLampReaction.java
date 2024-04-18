package com.vdn.lampbearer.action.reactions.items;

import com.vdn.lampbearer.action.actions.items.LightLampAction;
import com.vdn.lampbearer.action.actions.items.PutOutLampAction;
import com.vdn.lampbearer.action.reactions.Reaction;
import com.vdn.lampbearer.attributes.LightSourceAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.World;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class LightLampReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity target, AbstractEntity lamp, GameContext context) {
        Optional<LightSourceAttr> lightAttr =
                lamp.findAttribute(LightSourceAttr.class);
        if (lightAttr.isEmpty()) return false;

        World world = context.getWorld();
        lightAttr.get().setOn(true);

        lightAttr.get().getLight().setPosition(target.getPosition().to2DPosition());
        world.addDynamicLight(target, lightAttr.get().getLight());
        world.updateLighting();

        lamp.removeAction(LightLampAction.class);
        lamp.getActions().add(PutOutLampAction.getInstance());

        log.info(String.format("%s's lighted %s", target.getName(), lamp.getName()));

        return true;
    }
}
