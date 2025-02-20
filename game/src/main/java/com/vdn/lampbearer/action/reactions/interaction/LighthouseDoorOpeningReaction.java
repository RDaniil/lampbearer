package com.vdn.lampbearer.action.reactions.interaction;

import com.vdn.lampbearer.action.actions.interaction.DoorCloseAction;
import com.vdn.lampbearer.action.actions.interaction.DoorOpenAction;
import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.attributes.occupation.BlockOccupier;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.entites.item.LighthouseKey;
import com.vdn.lampbearer.entites.item.LighthouseLamp;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.builder.component.LabelBuilder;
import org.hexworks.zircon.api.builder.graphics.CharacterTileStringBuilder;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.graphics.CharacterTileString;
import org.hexworks.zircon.api.graphics.StyleSet;
import org.hexworks.zircon.internal.graphics.DefaultCharacterTileString;

import java.util.Optional;

@Slf4j
public class LighthouseDoorOpeningReaction extends DoorOpeningReaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        Optional<InventoryAttr> inventoryOpt = initiator.findAttribute(InventoryAttr.class);
        if (inventoryOpt.isEmpty()) return false;
        Optional<AbstractItem> lighthouseLamp = inventoryOpt.get()
                .findItem(LighthouseKey.class);

        if(lighthouseLamp.isEmpty()) {
            printMessage(context);
            return false;
        }
        BlockType blockType = BlockType.OPENED_DOOR_LIGHTHOUSE;

        target.setTile(TileRepository.getTile(blockType));
        target.removeAttribute(BlockOccupier.class);
        target.removeAction(DoorOpenAction.class);
        target.getActions().add(DoorCloseAction.getInstance());
        context.getWorld().updateBlockContent(target.getPosition());

        return true;
    }

    private static void printMessage(GameContext context) {
        context.getLogArea().addInlineComponent(
                Components.label().withText("Door is locked. I should find a ").build());

        context.getLogArea()
                .addInlineComponent(
                        Components.label()
                                .withText("key ¥")
                                .withComponentRenderer((graphics, cntxt) -> {
                                    CharacterTileString str = CharacterTileStringBuilder.newBuilder()
                                            .withText("key ¥")
                                            .withForegroundColor(TileColor.fromString("#f12d23"))
                                            .build();
                                    graphics.draw(str);
                                }).build());

        context.getLogArea()
                .addInlineComponent(
                        Components.label().withText(" somewhere").build());

        context.getLogArea().commitInlineElements();
    }
}
