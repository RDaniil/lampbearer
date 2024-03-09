package com.vdn.lampbearer.entites.behavior.player;

import com.vdn.lampbearer.action.TargetedReaction;
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
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
@AllArgsConstructor
public class PlayerTargetBehavior extends PlayerBehavior {

    private TargetedReaction reactionToExecute;
    private Position selectedPosition;
    private Tile selectTile = Tile.newBuilder().withCharacter(' ')
            .withForegroundColor(TileColor.fromString("#FFAC4D").withAlpha(100))
            .withModifiers(Modifiers.blink())
            .withBackgroundColor(TileColor.fromString("#E7D5B6").withAlpha(200))
            .build();
    private boolean targetActionExecuted;


    public PlayerTargetBehavior(TargetedReaction targetedReaction) {
        super();
        this.reactionToExecute = targetedReaction;
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


    @NotNull
    @Override
    public PlayerBehavior next(Player player, GameContext context) {
        var event = context.getEvent();
        if (!(event instanceof KeyboardEvent)) return this;

        KeyboardEvent keyboardEvent = (KeyboardEvent) event;

        if (isCanceledTargeting(keyboardEvent) || targetActionExecuted) {
            clearPreviousTargetTile(context);
            return new PlayerMoveAndAttackBehavior();
        }

        return this;
    }


    private static boolean isCanceledTargeting(KeyboardEvent keyboardEvent) {
        return keyboardEvent.getCode().equals(KeyCode.KEY_C) ||
                keyboardEvent.getCode().equals(KeyCode.ESCAPE);
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
        } else if (keyboardEvent.getCode().equals(KeyCode.ENTER)) {
            targetActionExecuted = reactionToExecute.execute(
                    context.getPlayer(),
                    context.getWorld().getEntityOrBlockEntityAt(convertToWorldPosition(context, selectedPosition)),
                    context
            );
            return targetActionExecuted;
        }

        selectPosition(context, selectedPosition);
        return false;
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
}
