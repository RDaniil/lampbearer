package com.vdn.lampbearer.views.fragments;

import com.vdn.lampbearer.dto.EnemyInfo;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.services.RandomService;
import com.vdn.lampbearer.services.ScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Component;
import org.hexworks.zircon.api.component.Fragment;
import org.hexworks.zircon.api.component.HBox;
import org.hexworks.zircon.api.component.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.hexworks.zircon.api.ComponentDecorations.noDecoration;

/**
 * UI-компонент экрана конца игры
 */
@Slf4j
@RequiredArgsConstructor
public class GameOverFragment implements Fragment {

    private final GameContext context;


    @NotNull
    @Override
    public Component getRoot() {
        Map<EnemyInfo, Integer> killedEnemiesToCount = context.getScoreService().getKilledEnemiesMap();

        VBox modal = Components.vbox()
                .withPreferredSize(50, killedEnemiesToCount.size() + 4)
                .build();

        var youKilledText = Components.label()
                .withDecorations(noDecoration())
                .withText("You killed: ")
                .build();

        modal.addComponent(youKilledText);

        for (Map.Entry<EnemyInfo, Integer> enemyToCounter : killedEnemiesToCount.entrySet()) {
            modal.addComponent(createComponentFromEnemyCounter(enemyToCounter));
        }


        modal.addComponent(createMoveCounterComponent(context.getScoreService()));



        var seedText = Components.label()
                .withDecorations(noDecoration())
                .withText("Seed: " + RandomService.getSeed())
                .build();
        modal.addComponent(seedText);

        //TODO: дошел ли до маяка, для маяка отдельное сообщение о том что игрок победил

        return modal;
    }

    private Component createMoveCounterComponent(ScoreService scoreService) {
        HBox movesComponent = Components.hbox()
                .withPreferredSize(scoreService.getPlayerMoveCounter().toString().length() + 16, 1)
                .build();

        var movesLabel = Components.label()
                .withDecorations(noDecoration())
                .withText("You made " + scoreService.getPlayerMoveCounter() + " moves ")
                .build();
        movesComponent.addComponent(movesLabel);

        return movesComponent;
    }

    private Component createComponentFromEnemyCounter(Map.Entry<EnemyInfo, Integer> enemyToCounter) {
        HBox enemyCounterInfo = Components.hbox()
                .withPreferredSize(enemyToCounter.getKey().getName().length() + 5, 1)
                .build();

        var enemyIcon = Components.icon()
                .withIcon(enemyToCounter.getKey().getTile())
                .build();

        var enemyInfoComponent = Components.label()
                .withDecorations(noDecoration())
                .withText(" " + enemyToCounter.getKey().getName() + ": " + enemyToCounter.getValue())
                .build();

        enemyCounterInfo.addComponent(enemyIcon);
        enemyCounterInfo.addComponent(enemyInfoComponent);
        return enemyCounterInfo;
    }
}
