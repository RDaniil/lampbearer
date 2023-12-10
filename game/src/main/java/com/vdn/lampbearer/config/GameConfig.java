package com.vdn.lampbearer.config;

import org.hexworks.zircon.api.CP437TilesetResources;
import org.hexworks.zircon.api.ColorThemes;
import org.hexworks.zircon.api.application.AppConfig;
import org.hexworks.zircon.api.component.ColorTheme;

public class GameConfig {

    public static final ColorTheme THEME = ColorThemes.zenburnVanilla();
    public static final int WINDOW_WIDTH = 80;
    public static final int WINDOW_HEIGHT = 50;
    public static final int SIDEBAR_WIDTH = 20;
    public static final int SIDEBAR_HEIGHT = 50;
    public static final int LOG_AREA_HEIGHT = 5;

    public static AppConfig getAppConfig() {
        return AppConfig.newBuilder()
                .withDefaultTileset(CP437TilesetResources.rogueYun16x16())
                .withSize(WINDOW_WIDTH, WINDOW_HEIGHT)
                .build();
    }
}
