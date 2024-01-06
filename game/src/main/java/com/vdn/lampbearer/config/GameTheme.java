package com.vdn.lampbearer.config;

import org.hexworks.zircon.api.builder.component.ColorThemeBuilder;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.component.ColorTheme;

public class GameTheme {

    public static ColorTheme getTheme() {
        return ColorThemeBuilder.newBuilder()
                .withAccentColor(TileColor.fromString("#FFAC4D"))
                .withPrimaryBackgroundColor(TileColor.fromString("#100909"))
                .withPrimaryForegroundColor(TileColor.fromString("#E7D5B6"))
                .withSecondaryBackgroundColor(TileColor.fromString("#2D151A"))
                .withSecondaryForegroundColor(TileColor.fromString("#A94F42"))
                .build();
    }
}
