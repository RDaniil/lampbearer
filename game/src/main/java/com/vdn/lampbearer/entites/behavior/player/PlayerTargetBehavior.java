package com.vdn.lampbearer.entites.behavior.player;

import com.vdn.lampbearer.action.reactions.LookReaction;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.game.GameContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hexworks.zircon.api.Modifiers;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.hexworks.zircon.api.uievent.UIEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class PlayerTargetBehavior extends PlayerBehavior {

    private static final Set<KeyCode> MOVEMENT_KEYS = new HashSet<>(
            List.of(KeyCode.KEY_W, KeyCode.KEY_A, KeyCode.KEY_S, KeyCode.KEY_D, KeyCode.ENTER,
                    KeyCode.SPACE, KeyCode.ESCAPE, KeyCode.KEY_C, KeyCode.KEY_L)
    );
    private LookReaction reactionToExecute;
    private Position selectedPosition;
    private Tile selectTile = Tile.newBuilder().withCharacter(' ')
            .withForegroundColor(TileColor.fromString("#FFAC4D").withAlpha(100))
            .withModifiers(Modifiers.blink())
            .withBackgroundColor(TileColor.fromString("#E7D5B6").withAlpha(200))
            .build();


    public PlayerTargetBehavior(LookReaction lookReaction) {
        super();
        this.reactionToExecute = lookReaction;
    }


    @Override
    public boolean act(Player player, GameContext context) {
        var event = context.getEvent();
        if (event instanceof KeyboardEvent) {
            if (selectedPosition == null) {
                selectedPosition = player.getPosition()
                        .minus(context.getWorld().getVisibleOffset())
                        .to2DPosition();
            }
            KeyboardEvent keyboardEvent = (KeyboardEvent) event;
            return moveTarget(context, keyboardEvent);
        }
        return false;
    }


    private boolean moveTarget(GameContext context, KeyboardEvent keyboardEvent) {
        clearPreviousTargetTile(context);

        if (keyboardEvent.getCode().equals(KeyCode.KEY_W)) {
            selectedPosition = selectedPosition.withRelativeY(-1);
        } else if (keyboardEvent.getCode().equals(KeyCode.KEY_A)) {
            selectedPosition = selectedPosition.withRelativeX(-1);
        } else if (keyboardEvent.getCode().equals(KeyCode.KEY_S)) {
            selectedPosition = selectedPosition.withRelativeY(1);
        } else if (keyboardEvent.getCode().equals(KeyCode.KEY_D)) {
            selectedPosition = selectedPosition.withRelativeX(1);
        } else if (keyboardEvent.getCode().equals(KeyCode.KEY_C) || keyboardEvent.getCode().equals(KeyCode.ESCAPE)) {
            context.getPlayer().changeBehavior(new PlayerMoveAndAttackBehavior(), context);
            return false;
        } else if (keyboardEvent.getCode().equals(KeyCode.ENTER)) {
            return reactionToExecute.execute(context.getPlayer(),
                    context.getWorld().getEntityAt(
                            convertToWorldPosition(context, selectedPosition)), context);
        }
        selectPosition(context, selectedPosition);
        return true;
    }


    private void clearPreviousTargetTile(GameContext context) {
        context.getScreen().getLayerAtOrNull(2).draw(Tile.empty(), selectedPosition);
    }


    private void selectPosition(GameContext context, Position position) {
        context.getScreen().getLayerAtOrNull(2).draw(selectTile, position);
    }


    private Position convertToWorldPosition(GameContext context, Position position) {
        return position.plus(context.getWorld().getVisibleOffset().to2DPosition());
    }

    public boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        context.getLogArea()
                .addParagraph(String.format("This is %s", target.getName()), false, 0);
        context.getLogArea()
                .addParagraph(target.getDescription(), false, 0);
        return true;
    }


    @Override
    public boolean isUiEventApplicable(UIEvent event) {
        KeyboardEvent keyboardEvent = (KeyboardEvent) event;
        return keyboardEvent != null && MOVEMENT_KEYS.contains(keyboardEvent.getCode())
                && !keyboardEvent.getCtrlDown()
                && !keyboardEvent.getAltDown()
                && !keyboardEvent.getShiftDown();
    }
}
