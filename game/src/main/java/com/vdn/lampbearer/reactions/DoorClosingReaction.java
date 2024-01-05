package com.vdn.lampbearer.reactions;

import com.vdn.lampbearer.action.interaction.DoorCloseAction;
import com.vdn.lampbearer.action.interaction.DoorOpenAction;
import com.vdn.lampbearer.attributes.arrangement.VerticalArrangement;
import com.vdn.lampbearer.attributes.occupation.DynamicBlockOccupier;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.views.TileRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DoorClosingReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        if (target.findAction(DoorCloseAction.class).isEmpty()) return false;

        boolean isVertical = target.findAttribute(VerticalArrangement.class).isPresent();
        target.setTile(isVertical ? TileRepository.V_CLOSED_DOOR : TileRepository.H_CLOSED_DOOR);
        target.getAttributes().add(DynamicBlockOccupier.getInstance());
        target.removeAction(DoorCloseAction.class);
        target.getActions().add(DoorOpenAction.getInstance());
        context.getWorld().updateBlockContent(target.getPosition());

        context.getLogArea()
                .addParagraph(String.format("%s's been closed", target.getName()), false, 10);

        return true;
    }
}
