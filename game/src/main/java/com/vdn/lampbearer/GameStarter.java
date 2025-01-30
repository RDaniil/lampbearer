package com.vdn.lampbearer;

import com.vdn.lampbearer.exception.GameOverException;
import com.vdn.lampbearer.game.Game;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.services.ScoreService;
import com.vdn.lampbearer.views.GameOverView;
import com.vdn.lampbearer.views.PlayView;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hexworks.zircon.api.component.LogArea;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.hexworks.zircon.api.uievent.KeyboardEventType;
import org.hexworks.zircon.api.uievent.Processed;

@RequiredArgsConstructor
public class GameStarter {

    private final PlayView playView;
    private KeyboardEvent event;


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

        GameContext gameContext = new GameContext(
                game.getWorld(),
                playView.getSidePanel(),
                event,
                game.getPlayer(),
                logArea,
                screen,
                new ScoreService());
        game.getWorld().initUi(gameContext);
        game.getWorld().updateUI();

        try {
            doMainLoop(gameContext, game);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private synchronized void notifyMainLoop() {
        notifyAll();
    }


    private synchronized void doMainLoop(GameContext gameContext, Game game) throws InterruptedException {
        while (true) {
            wait();

            //TODO: У org.hexworks.zircon.internal.game.impl.GameAreaComponentRenderer.render есть
            // публичный метод рендера, мб он как-то спасет
            // Если убрать мнгопоточку в GameStarter, модальные окна почему-то не отображаются.
            gameContext.setEvent(event);
            try {
                game.getWorld().update(gameContext);
            } catch (GameOverException e){
                new GameOverView(gameContext).show();

            }
            game.getWorld().updateUI();
        }
    }
}
