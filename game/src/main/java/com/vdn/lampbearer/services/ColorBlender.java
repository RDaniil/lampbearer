package com.vdn.lampbearer.services;

import org.hexworks.zircon.api.color.TileColor;

public class ColorBlender {

    private static final int MAX_COLOR_VALUE = 255;
    public static TileColor mixLightColors(TileColor firstColor, TileColor secondColor){
        return sumColorWithClamp(firstColor, secondColor);
    }

    private static TileColor sumColorWithClamp(TileColor firstColor, TileColor secondColor){
        return TileColor.defaultForegroundColor()
                .withRed(sumClamp(firstColor.getRed(),secondColor.getRed(), MAX_COLOR_VALUE))
                .withGreen(sumClamp(firstColor.getGreen(),secondColor.getGreen(), MAX_COLOR_VALUE))
                .withBlue(sumClamp(firstColor.getBlue(),secondColor.getBlue(), MAX_COLOR_VALUE))
                .withAlpha(sumClamp(firstColor.getAlpha(),secondColor.getAlpha(), MAX_COLOR_VALUE));
    }

    private static TileColor averageSum(TileColor firstColor, TileColor secondColor){
        int alpha = (firstColor.getAlpha() + secondColor.getAlpha()) / 2;
        int red = (firstColor.getRed() + secondColor.getRed()) / 2;
        int green = (firstColor.getGreen() + secondColor.getGreen()) / 2;
        int blue = (firstColor.getBlue() + secondColor.getBlue()) / 2;

        return TileColor.defaultForegroundColor().withAlpha(alpha).withRed(red).withBlue(blue).withGreen(green);
    }

    private static int sumClamp(int firstValue, int secondValue, int maxValue){
        int result = firstValue + secondValue;
        if (result > maxValue){
            result = maxValue;
        }
        return result;
    }
}
