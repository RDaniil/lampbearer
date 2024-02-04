package com.vdn.lampbearer.config;

import org.hexworks.zircon.api.CP437TilesetResources;
import org.hexworks.zircon.api.application.AppConfig;
import org.hexworks.zircon.api.component.ColorTheme;

public class GameConfig {

    public static final ColorTheme THEME = GameTheme.getTheme();
    public static final int WINDOW_WIDTH = 100;
    public static final int WINDOW_HEIGHT = 50;
    public static final int SIDEBAR_WIDTH = WINDOW_WIDTH / 4;
    public static final int SIDEBAR_HEIGHT = WINDOW_HEIGHT - 2;
    public static final int LOG_AREA_HEIGHT = WINDOW_HEIGHT / 5;

    public static AppConfig getAppConfig() {
        return AppConfig.newBuilder()
                .withDefaultTileset(CP437TilesetResources.anikki16x16())
                .withSize(WINDOW_WIDTH, WINDOW_HEIGHT)
                .withTitle("TMIQ III the Lampbearer")
//                .withFpsLimit(0)
                .withDebugMode(true)
//                .withDebugConfig(DebugConfig.newBuilder()
//                        .withRelaxBoundsCheck(true)
//                        .build())
                .build();
    }
}
