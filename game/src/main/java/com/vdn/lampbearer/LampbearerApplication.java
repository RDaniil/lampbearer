package com.vdn.lampbearer;

import com.vdn.lampbearer.config.GameConfig;
import com.vdn.lampbearer.game.GameBuilder;
import com.vdn.lampbearer.services.RandomService;
import com.vdn.lampbearer.services.builder.world.GenerativeWorldBuilder;
import com.vdn.lampbearer.views.PlayView;
import org.hexworks.zircon.api.SwingApplications;
import org.hexworks.zircon.api.grid.TileGrid;

public class LampbearerApplication {

    public static void main(String[] args) {
        RandomService.initRandom();

        TileGrid tileGrid = SwingApplications.startTileGrid(GameConfig.getAppConfig());
        var worldBuilderService = new GenerativeWorldBuilder();
        var gameBuilder = new GameBuilder(worldBuilderService);
        var playView = new PlayView(tileGrid, gameBuilder);
        GameStarter gameStarter = new GameStarter(playView);

        gameStarter.start();
    }

}
