package com.vdn.lampbearer.reactions;

import com.vdn.lampbearer.action.interaction.DoorClosingAction;
import com.vdn.lampbearer.action.interaction.DoorOpeningAction;
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
        if (target.findAction(DoorClosingAction.class).isEmpty()) return false;

        boolean isVertical = target.findAttribute(VerticalArrangement.class).isPresent();
        target.setTile(isVertical ? TileRepository.V_CLOSED_DOOR : TileRepository.H_CLOSED_DOOR);
        target.getAttributes().add(DynamicBlockOccupier.getInstance());
        target.removeAction(DoorClosingAction.class);
        target.getActions().add(DoorOpeningAction.getInstance());
        context.getWorld().updateBlockContent(target.getPosition());

        context.getLogArea()
                .addParagraph(String.format("%s's been closed", target.getName()), false, 10);

        return true;
    }
}
