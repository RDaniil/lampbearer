package com.vdn.lampbearer;

import com.vdn.lampbearer.game.Game;
import com.vdn.lampbearer.game.GameContext;
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
            notifyMainLoop();
            return Processed.INSTANCE;
        });

        GameContext gameContext = new GameContext(game.getWorld(), playView.getSidePanel(), event,
                game.getPlayer(),
                logArea, screen);
        game.getWorld().initUi(gameContext);
        game.getWorld().updateUI();

        doMainLoop(gameContext, game);
    }


    private synchronized void notifyMainLoop() {
        notifyAll();
    }


    private synchronized void doMainLoop(GameContext gameContext, Game game) throws InterruptedException {
        while (true) {
            wait();

            gameContext.setEvent(event);
            game.getWorld().update(gameContext);
            game.getWorld().updateUI();
        }
    }
}
