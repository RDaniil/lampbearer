package com.vdn.lampbearer.config;

import org.hexworks.zircon.api.CP437TilesetResources;
import org.hexworks.zircon.api.application.AppConfig;
import org.hexworks.zircon.api.application.DebugConfig;
import org.hexworks.zircon.api.component.ColorTheme;

public class GameConfig {

    public static final ColorTheme THEME = GameTheme.getTheme();
    public static final int WINDOW_WIDTH = 100;
    public static final int WINDOW_HEIGHT = 50;
    public static final int SIDEBAR_WIDTH = 30;
    public static final int SIDEBAR_HEIGHT = 50;
    public static final int LOG_AREA_HEIGHT = 10;

    public static AppConfig getAppConfig() {
        return AppConfig.newBuilder()
                .withDefaultTileset(CP437TilesetResources.anikki16x16())
                .withSize(WINDOW_WIDTH, WINDOW_HEIGHT)
                .withFpsLimit(60)
                .withDebugMode(true)
                .withDebugConfig(DebugConfig.newBuilder()
                        .withRelaxBoundsCheck(true)
                        .build())
                .build();
    }
}
