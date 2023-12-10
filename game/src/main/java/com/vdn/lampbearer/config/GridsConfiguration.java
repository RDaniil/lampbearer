package com.vdn.lampbearer.config;

import org.hexworks.zircon.api.SwingApplications;
import org.hexworks.zircon.api.grid.TileGrid;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GridsConfiguration {

    @Bean
    public TileGrid playViewTileGrid() {
        return SwingApplications.startTileGrid(GameConfig.getAppConfig());
    }
}
