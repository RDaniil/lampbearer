package com.vdn.lampbearer.action.reactions.interaction;

import com.vdn.lampbearer.action.actions.interaction.DoorCloseAction;
import com.vdn.lampbearer.action.actions.interaction.DoorOpenAction;
import com.vdn.lampbearer.action.actions.interaction.LampbearerDoorOpenAction;
import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.attributes.occupation.BlockOccupier;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.entites.item.LighthouseLamp;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.builder.graphics.CharacterTileStringBuilder;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.graphics.CharacterTileString;

import java.util.Optional;

@Slf4j
public class LambearerDoorOpeningReaction extends DoorOpeningReaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        Optional<InventoryAttr> inventoryOpt = initiator.findAttribute(InventoryAttr.class);
        if (inventoryOpt.isEmpty()) return false;
        Optional<AbstractItem> lighthouseLamp = inventoryOpt.get()
                .findItem(LighthouseLamp.class);

        if(lighthouseLamp.isEmpty()) {
            return printMessage(context);
        }

        return executeReaction(target, context);
    }

    private boolean executeReaction(AbstractEntity target, GameContext context) {
        if (target.findAction(LampbearerDoorOpenAction.class).isEmpty()) return false;

        BlockType blockType = BlockType.OPENED_DOOR_LAMBEARER_HOUSE;

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

    private static boolean printMessage(GameContext context) {
        context.getLogArea().addInlineComponent(
                Components.label().withText("I should get ").build());

        context.getLogArea()
                .addInlineComponent(
                        Components.label()
                                .withText("lighthouse lamp ♦")
                                .withComponentRenderer((graphics, cntxt) -> {
                                    CharacterTileString str = CharacterTileStringBuilder.newBuilder()
                                            .withText("lighthouse lamp ♦")
                                            .withForegroundColor(TileColor.fromString("#ffba37"))
                                            .build();
                                    graphics.draw(str);
                                }).build());

        context.getLogArea()
                .addInlineComponent(
                        Components.label().withText(" before going out").build());

        context.getLogArea().commitInlineElements();
        return false;
    }
}
