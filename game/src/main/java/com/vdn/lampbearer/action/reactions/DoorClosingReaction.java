package com.vdn.lampbearer.action.reactions;

import com.vdn.lampbearer.action.Reaction;
import com.vdn.lampbearer.action.actions.DoorCloseAction;
import com.vdn.lampbearer.action.actions.DoorOpenAction;
import com.vdn.lampbearer.attributes.arrangement.VerticalArrangement;
import com.vdn.lampbearer.attributes.occupation.DynamicBlockOccupier;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.views.BlockTypes;
import com.vdn.lampbearer.views.TileRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DoorClosingReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        if (target.findAction(DoorCloseAction.class).isEmpty()) return false;

        boolean isVertical = target.findAttribute(VerticalArrangement.class).isPresent();
        BlockTypes blockTypes = isVertical ? BlockTypes.V_CLOSED_DOOR : BlockTypes.H_CLOSED_DOOR;

        target.setTile(TileRepository.getTile(blockTypes));
        target.getAttributes().add(DynamicBlockOccupier.getInstance());
        target.removeAction(DoorCloseAction.class);
        target.getActions().add(DoorOpenAction.getInstance());
        context.getWorld().updateBlockContent(target.getPosition());

        context.getLogArea()
                .addParagraph(String.format("%s's been closed", target.getName()), false, 0);

        target.setName(GameBlockFactory.returnGameBlock(blockTypes).getName());

        return true;
    }
}
