package com.vdn.lampbearer.entites.behavior.player;

import com.vdn.lampbearer.action.actions.AbstractPickUpItemAction;
import com.vdn.lampbearer.attributes.InventoryAttr;
import com.vdn.lampbearer.dto.ItemUseReactionContextDto;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.views.inventory.InventoryItemSelectModalView;
import lombok.SneakyThrows;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.hexworks.zircon.api.uievent.UIEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerInventoryInteractionBehavior extends PlayerBehavior {
    private static final Set<KeyCode> INVENTORY_KEYS = new HashSet<>(
            List.of(KeyCode.KEY_P, KeyCode.KEY_U)
    );


    @Override
    public boolean act(Player actor, GameContext context) {
        var event = context.getEvent();
        if (event instanceof KeyboardEvent) {
            KeyboardEvent keyboardEvent = (KeyboardEvent) event;
            return interactWithInventory(context, keyboardEvent);
        }
        return false;
    }


    @SneakyThrows
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
            //Здесь указываем что надо перейти в таргет, возвращаем фолс
//            player.changeBehavior(new PlayerTargetBehavior(actionDto));
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


    @Override
    public boolean isUiEventApplicable(UIEvent event) {
        KeyboardEvent keyboardEvent = (KeyboardEvent) event;
        return keyboardEvent != null && INVENTORY_KEYS.contains(keyboardEvent.getCode());
    }
}
