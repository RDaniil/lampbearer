package com.vdn.lampbearer.action.reactions.interaction;

import com.vdn.lampbearer.action.actions.interaction.DoorCloseAction;
import com.vdn.lampbearer.action.actions.interaction.DoorOpenAction;
import com.vdn.lampbearer.action.reactions.Reaction;
import com.vdn.lampbearer.attributes.occupation.BlockOccupier;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.builder.graphics.CharacterTileStringBuilder;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.graphics.CharacterTileString;
import org.hexworks.zircon.api.graphics.StyleSet;

@Slf4j
public class DoorOpeningReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        if (target.findAction(DoorOpenAction.class).isEmpty()) return false;

        BlockType blockType = BlockType.OPENED_DOOR;

        target.setTile(TileRepository.getTile(blockType));
        target.removeAttribute(BlockOccupier.class);
        target.removeAction(DoorOpenAction.class);
        target.getActions().add(DoorCloseAction.getInstance());
        context.getWorld().updateBlockContent(target.getPosition());

        context.getLogArea()
                .addParagraph(String.format("%s's been opened", target.getName()), false, 0);

        target.setName(GameBlockFactory.returnGameBlock(blockType).getName());

        return true;
    }
}
