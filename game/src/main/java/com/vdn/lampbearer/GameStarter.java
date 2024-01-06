package com.vdn.lampbearer;

import com.vdn.lampbearer.game.Game;
import com.vdn.lampbearer.views.PlayView;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hexworks.zircon.api.component.LogArea;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.hexworks.zircon.api.uievent.KeyboardEventType;
import org.hexworks.zircon.api.uievent.Processed;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameStarter {

    private final PlayView playView;
    private KeyboardEvent event;


    @SneakyThrows
    public void start() {
        playView.dock();
        Game game = playView.getGame();
        Screen screen = playView.getScreen();
        LogArea logArea = playView.getLogArea();

        screen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED, (event, uiEventPhase) -> {
            this.event = event;
            return Processed.INSTANCE;
        });

        while (true) {
            if (event != null) {
                game.getWorld().update(playView.getSidePanel(), event, game, logArea, screen);
                event = null;
            }
            //TODO: Переделать на poll
            Thread.sleep(10);
        }
    }
}
