package com.vdn.lampbearer.entites.behavior.player;

import com.vdn.lampbearer.action.actions.inventory.AbstractPickUpItemAction;
import com.vdn.lampbearer.action.reactions.ShootFirearmReaction;
import com.vdn.lampbearer.action.reactions.TargetedReaction;
import com.vdn.lampbearer.action.reactions.ThrowReaction;
import com.vdn.lampbearer.attributes.inventory.InventoryAttr;
import com.vdn.lampbearer.dto.ItemUseReactionContextDto;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.views.inventory.InventoryItemSelectModalView;
import lombok.SneakyThrows;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerInventoryInteractionBehavior extends PlayerBehavior {

    @Override
    public boolean act(Player actor, GameContext context) {
        var event = context.getEvent();
        if (event instanceof KeyboardEvent) {
            KeyboardEvent keyboardEvent = (KeyboardEvent) event;
            return interactWithInventory(context, keyboardEvent);
        }
        return false;
    }


    @NotNull
    @Override
    public PlayerBehavior next(Player player, GameContext context) {
        var event = context.getEvent();
        if (!(event instanceof KeyboardEvent)) return this;

        KeyboardEvent keyboardEvent = (KeyboardEvent) event;

        if (isMovement(keyboardEvent)) return new PlayerMoveAndAttackBehavior();
        if (isInteraction(keyboardEvent)) return new PlayerInteractionBehavior();
        if (isThrowAction(keyboardEvent)) return new PlayerTargetBehavior(new ThrowReaction());
        if (isRevolverAction(keyboardEvent))
            return new PlayerTargetBehavior(new ShootFirearmReaction());

        return this;
    }


    private boolean interactWithInventory(GameContext context, KeyboardEvent keyboardEvent) {
        Player player = context.getPlayer();
        if (keyboardEvent.getCode().equals(KeyCode.KEY_P)) {
            var pickupableItem = context.getWorld().getByAction(player.getPosition(),
                    AbstractPickUpItemAction.class);
            if (pickupableItem.isEmpty()) return false;

            //TODO: Выбор поднимаемого предмета
            pickupableItem.get().findAction(AbstractPickUpItemAction.class).get()
                    .createReaction().execute(player, pickupableItem.get(), context);
        } else if (keyboardEvent.getCode().equals(KeyCode.KEY_U)) {
            var actionDto = showItemActionModal(context);
            if (actionDto == null) return false;

            if (actionDto.getReaction() instanceof TargetedReaction) {
                player.setBehavior(new PlayerTargetBehavior((TargetedReaction) actionDto.getReaction()));
                return false;
            }

            return actionDto.getReaction().execute(
                    actionDto.getInitiator(),
                    actionDto.getTarget(),
                    actionDto.getContext());
        }

        return true;
    }


    @Nullable
    public ItemUseReactionContextDto showItemActionModal(GameContext context) {
        return InventoryItemSelectModalView.showItemActionModal(context,
                context.getPlayer().findAttribute(InventoryAttr.class).get());
    }
}
