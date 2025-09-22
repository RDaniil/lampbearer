package com.vdn.lampbearer.action.reactions.revolver;

import com.vdn.lampbearer.action.reactions.Reaction;
import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.entites.item.firearm.Revolver;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.builder.graphics.CharacterTileStringBuilder;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.graphics.CharacterTileString;
import org.hexworks.zircon.internal.data.DefaultCharacterTile;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@Slf4j
public class CheckChamberReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        Optional<InventoryAttr> inventoryOpt = initiator.findAttribute(InventoryAttr.class);
        if (inventoryOpt.isEmpty()) return false;

        Optional<AbstractItem> foundFirearm = inventoryOpt.get().findItem(Revolver.class);
        if (foundFirearm.isEmpty()) {
            return false;
        }

        if (!(foundFirearm.get() instanceof Revolver)) {
            return false;
        }

        Revolver revolver = (Revolver) foundFirearm.get();
        List<Tile> chamberContents = revolver.getChamberContents();

        context.getLogArea().addInlineComponent(Components.label().withText("Revolver chamber: ").build());

        context.getLogArea()
                .addInlineComponent(
                        Components.label()
                                .withText("******************")
                                .withComponentRenderer((graphics, cntxt) -> {
                                    CharacterTileString result = CharacterTileStringBuilder.newBuilder().withText("").build();
                                    for (int i = 0; i < chamberContents.size(); i++) {
                                        result = result.plus(getChamberContents(chamberContents, i));
                                    }
                                    graphics.draw(result);
                                }).build());

        context.getLogArea().commitInlineElements();

        return true;
    }

    @NotNull
    private static CharacterTileString getChamberContents(List<Tile> chamberContents, int chamberNumber) {
        Tile chamberContent = chamberContents.get(chamberNumber);
        String roundChar = String.valueOf(((DefaultCharacterTile) chamberContent).getCharacter());
        roundChar = roundChar.trim().isEmpty() ? "_": roundChar;

        CharacterTileString ob = CharacterTileStringBuilder.newBuilder()
                .withText("[")
                .withForegroundColor(TileColor.fromString("#ffba37"))
                .build();

        CharacterTileStringBuilder round = CharacterTileStringBuilder.newBuilder()
                .withText(roundChar)
                .withForegroundColor(chamberContent.getForegroundColor());
        if(chamberNumber == 0){
            //Первая камора всегда выделена чтобы было понятно какой следующий выстрел
            round.withBackgroundColor(TileColor.fromString("#ffba37"));
        }
        CharacterTileString cb = CharacterTileStringBuilder.newBuilder()
                .withText("]")
                .withForegroundColor(TileColor.fromString("#ffba37"))
                .build();
        return ob.plus(round.build().plus(cb));
    }
}
