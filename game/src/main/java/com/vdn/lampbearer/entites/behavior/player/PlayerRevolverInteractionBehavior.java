package com.vdn.lampbearer.entites.behavior.player;

import com.vdn.lampbearer.action.reactions.ShootFirearmReaction;
import com.vdn.lampbearer.attributes.InventoryAttr;
import com.vdn.lampbearer.dto.ItemUseReactionContextDto;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.entites.item.firearm.Revolver;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.views.inventory.InventoryItemSelectModalView;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PlayerRevolverInteractionBehavior extends PlayerBehavior {

    @Override
    public boolean act(Player actor, GameContext context) {
        var event = context.getEvent();
        if (event instanceof KeyboardEvent) {
            KeyboardEvent keyboardEvent = (KeyboardEvent) event;
            return interactWithRevolver(context, keyboardEvent);
        }
        return false;
    }


    @NotNull
    @Override
    public PlayerBehavior next(Player player, GameContext context) {
        var event = context.getEvent();
        if (!(event instanceof KeyboardEvent)) return this;

        KeyboardEvent keyboardEvent = (KeyboardEvent) event;

        if (isShooting(keyboardEvent)) return new PlayerTargetBehavior(new ShootFirearmReaction());

        if (isMovement(keyboardEvent)) return new PlayerMoveAndAttackBehavior();
        if (isInteraction(keyboardEvent)) return new PlayerInteractionBehavior();
        if (isInventoryAction(keyboardEvent)) return new PlayerInventoryInteractionBehavior();

        return this;
    }


    private boolean interactWithRevolver(GameContext context, KeyboardEvent keyboardEvent) {
        Player player = context.getPlayer();
        if (isShooting(keyboardEvent)) {
            Optional<InventoryAttr> inventoryOpt = player.findAttribute(InventoryAttr.class);
            if (inventoryOpt.isEmpty()) return false;

            Optional<AbstractItem> revolver = inventoryOpt.get().findItem(Revolver.class);
            if (revolver.isEmpty()) {
                context.getLogArea().addParagraph("I don't have revolver", false, 0);
            }
            return false;
        }

        return true;
    }


    private static boolean isShooting(KeyboardEvent keyboardEvent) {
        return keyboardEvent.getCode().equals(KeyCode.KEY_R)
                && !keyboardEvent.getCtrlDown()
                && !keyboardEvent.getAltDown()
                && !keyboardEvent.getShiftDown();
    }


    @Nullable
    public ItemUseReactionContextDto showItemActionModal(GameContext context) {
        return InventoryItemSelectModalView.showItemActionModal(context,
                context.getPlayer().findAttribute(InventoryAttr.class).get());
    }
}
