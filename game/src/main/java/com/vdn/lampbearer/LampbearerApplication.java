package com.vdn.lampbearer;

import com.vdn.lampbearer.config.GameConfig;
import com.vdn.lampbearer.game.Game;
import com.vdn.lampbearer.game.GameBuilder;
import com.vdn.lampbearer.services.builder.world.SimpleWorldBuilderService;
import com.vdn.lampbearer.views.PlayView;
import org.hexworks.zircon.api.SwingApplications;
import org.hexworks.zircon.api.grid.TileGrid;

public class LampbearerApplication {


    public static void main(String[] args) {
        TileGrid tileGrid = SwingApplications.startTileGrid(GameConfig.getAppConfig());
        var worldBuilderService = new SimpleWorldBuilderService();
        var gameBuilder = new GameBuilder(worldBuilderService);
        var playView = new PlayView(tileGrid, gameBuilder);
        GameStarter gameStarter = new GameStarter(playView);

        gameStarter.start();
    }

}
