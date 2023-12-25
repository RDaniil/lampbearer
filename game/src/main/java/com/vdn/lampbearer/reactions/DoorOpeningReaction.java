package com.vdn.lampbearer.reactions;

import com.vdn.lampbearer.action.interaction.DoorClosingAction;
import com.vdn.lampbearer.action.interaction.DoorOpeningAction;
import com.vdn.lampbearer.attributes.BlockOccupier;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.views.TileRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DoorOpeningReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        if (target.findAction(DoorOpeningAction.class).isEmpty()) return false;

        target.setTile(TileRepository.OPENED_DOOR);
        target.removeAttribute(BlockOccupier.class);
        target.removeAction(DoorOpeningAction.class);
        target.getActions().add(DoorClosingAction.getInstance());
        context.getWorld().updateBlockContent(target.getPosition());

        context.getLogArea()
                .addParagraph(String.format("%s's been opened", target.getName()), false, 10);

        return true;
    }
}
