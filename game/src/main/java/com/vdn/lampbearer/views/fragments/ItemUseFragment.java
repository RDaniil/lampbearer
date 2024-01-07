package com.vdn.lampbearer.views.fragments;

import com.vdn.lampbearer.action.Action;
import com.vdn.lampbearer.config.GameConfig;
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
import org.hexworks.zircon.api.uievent.KeyboardEventType;
import org.hexworks.zircon.api.uievent.MouseEventType;
import org.hexworks.zircon.api.uievent.UIEventPhase;
import org.hexworks.zircon.api.uievent.UIEventResponse;
import org.hexworks.zircon.internal.component.modal.EmptyModalResult;
import org.jetbrains.annotations.NotNull;

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
        for (Action<?> action : item.getActions()) {
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
                        action.createReaction().execute(context.getPlayer(), item, context);
                        containingModal.close(EmptyModalResult.INSTANCE);
                    }
                    return UIEventResponse.processed();
                });

        containingModal.handleKeyboardEvents(
                KeyboardEventType.KEY_PRESSED,
                (event, uiEventPhase) -> {
                    if (uiEventPhase.equals(UIEventPhase.TARGET)) {
                        if (event.getKey().toUpperCase().charAt(0) == firstLetter) {
                            action.createReaction().execute(context.getPlayer(), item, context);
                            containingModal.close(EmptyModalResult.INSTANCE);
                        }
                    }
                    return UIEventResponse.processed();
                });

        return actionButton;
    }
}
