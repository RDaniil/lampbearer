package com.vdn.lampbearer.views.fragments;

import com.vdn.lampbearer.action.actions.Action;
import com.vdn.lampbearer.action.reactions.Reaction;
import com.vdn.lampbearer.config.GameConfig;
import com.vdn.lampbearer.dto.ItemUseReactionContextDto;
import com.vdn.lampbearer.entites.item.AbstractItem;
import com.vdn.lampbearer.game.GameContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Component;
import org.hexworks.zircon.api.component.Fragment;
import org.hexworks.zircon.api.component.HBox;
import org.hexworks.zircon.api.component.VBox;
import org.hexworks.zircon.api.component.modal.Modal;
import org.hexworks.zircon.api.component.modal.ModalResult;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.fragment.menu.MenuItemSelected;
import org.hexworks.zircon.api.fragment.menu.SelectionCancelled;
import org.hexworks.zircon.api.uievent.*;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hexworks.zircon.api.ComponentDecorations.noDecoration;

/**
 * UI-компонент выбора действия с предметом
 */
@Slf4j
@RequiredArgsConstructor
public class ItemUseFragment implements Fragment {

    private final AbstractItem item;
    private final GameContext context;
    private final Modal<ModalResult> containingModal;


    @NotNull
    @Override
    public Component getRoot() {
        VBox actionsList = Components.vbox()
                .withPreferredSize(25, item.getActions().size())
                .build();

        List<Action<?>> sortedActions = item.getActions().stream()
                .sorted(Comparator.comparing(Action::getName))
                .collect(Collectors.toList());

        for (Action<?> action : sortedActions) {
            var actionButton = getActionButton(action);
            actionsList.addComponent(actionButton);
        }
        return actionsList;
    }


    private HBox getActionButton(Action<?> action) {
        HBox actionButton = Components.hbox()
                .withPreferredSize(action.getName().length(), 1)
                .withDecorations(noDecoration())
                .build();
        char firstLetter = action.getName().charAt(0);
        String remainingLetters = action.getName().substring(1);

        actionButton.addComponent(Components.icon()
                .withIcon(Tile.newBuilder().withCharacter(firstLetter)
                        .withBackgroundColor(GameConfig.THEME.getAccentColor())
                        .withForegroundColor(GameConfig.THEME.getSecondaryForegroundColor())
                        .build()));
        actionButton.addComponent(Components.button()
                .withText(remainingLetters)
                .withDecorations(noDecoration())
                .build());

        actionButton.handleMouseEvents(
                MouseEventType.MOUSE_PRESSED,
                (mouseEvent, uiEventPhase) -> {
                    if (uiEventPhase.equals(UIEventPhase.CAPTURE)) {
                        sendSelectedActionToModal(action.createReaction());
                    }
                    return UIEventResponse.processed();
                });

        containingModal.handleKeyboardEvents(
                KeyboardEventType.KEY_PRESSED,
                (event, uiEventPhase) -> {
                    if (uiEventPhase.equals(UIEventPhase.TARGET)) {
                        if (event.getKey().toUpperCase().charAt(0) == firstLetter) {
                            sendSelectedActionToModal(action.createReaction());
                        } else if (event.getCode().equals(KeyCode.ESCAPE)) {
                            containingModal.close(SelectionCancelled.INSTANCE);
                        }
                    }
                    return UIEventResponse.processed();
                });

        return actionButton;
    }


    private void sendSelectedActionToModal(Reaction action) {
        ItemUseReactionContextDto reactionContext =
                new ItemUseReactionContextDto(context.getPlayer(), item,
                        context, action);
        containingModal.close(new MenuItemSelected<>(reactionContext));
    }
}
