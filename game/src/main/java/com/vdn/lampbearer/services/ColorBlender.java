package com.vdn.lampbearer.services;

import org.hexworks.zircon.api.color.TileColor;

public class ColorBlender {

    private static final int MAX_COLOR_VALUE = 255;
    
    public enum BlendMode {
        SCREEN_BLEND,     // Soft, realistic light mixing (default)
        WEIGHTED_BLEND,   // Very soft, subtle mixing
        AVERAGE_SUM,      // Simple averaging
        ADDITIVE_CLAMP    // Original harsh blending
    }
    
    private static final BlendMode CURRENT_BLEND_MODE = BlendMode.SCREEN_BLEND;
    public static TileColor mixLightColors(TileColor firstColor, TileColor secondColor){
        switch (CURRENT_BLEND_MODE) {
            case SCREEN_BLEND:
                return softLightBlend(firstColor, secondColor);
            case WEIGHTED_BLEND:
                return weightedLightBlend(firstColor, secondColor);
            case AVERAGE_SUM:
                return averageSum(firstColor, secondColor);
            case ADDITIVE_CLAMP:
            default:
                return sumColorWithClamp(firstColor, secondColor);
        }
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
    
    /**
     * Soft blending without alpha dependency - uses simple weighted mixing
     * Creates gradual color transitions instead of harsh whites
     */
    private static TileColor softLightBlend(TileColor lightColor, TileColor baseColor) {
        // Use simple weighted average for predictable results
        // Light contributes 30%, base color contributes 70%
        float lightWeight = 0.3f;
        float baseWeight = 0.7f;
        
        int red = (int) (lightColor.getRed() * lightWeight + baseColor.getRed() * baseWeight);
        int green = (int) (lightColor.getGreen() * lightWeight + baseColor.getGreen() * baseWeight);
        int blue = (int) (lightColor.getBlue() * lightWeight + baseColor.getBlue() * baseWeight);
        
        return TileColor.defaultForegroundColor()
                .withRed(red)
                .withGreen(green)
                .withBlue(blue);
    }
    
    /**
     * Calculate light intensity based on color brightness
     */
    private static float getLightIntensity(TileColor color) {
        // Calculate perceived brightness using luminance formula
        float r = color.getRed() / 255.0f;
        float g = color.getGreen() / 255.0f;
        float b = color.getBlue() / 255.0f;
        
        // Standard luminance calculation
        return 0.299f * r + 0.587f * g + 0.114f * b;
    }
    
    /**
     * Multiplicative blending for softer light mixing
     * Prevents harsh white results while maintaining color characteristics
     */
    private static int multiplicativeBlend(int lightValue, int baseValue, float lightIntensity) {
        // Convert to 0-1 range
        float light = lightValue / 255.0f;
        float base = baseValue / 255.0f;
        
        // Reduce light intensity for softer blending (0.3 = 30% light effect)
        float adjustedIntensity = lightIntensity * 0.3f;
        
        // Blend using weighted average: base color + light effect
        float result = base + (light - base) * adjustedIntensity;
        
        // Clamp and convert back to 0-255 range
        return (int) Math.min(255, Math.max(0, result * 255));
    }
    
    /**
     * Alternative weighted blending approach for even softer results
     */
    private static TileColor weightedLightBlend(TileColor lightColor, TileColor baseColor) {
        float lightWeight = 0.3f; // Light contributes 30%
        float baseWeight = 0.7f;  // Base color contributes 70%
        
        int red = (int) (lightColor.getRed() * lightWeight + baseColor.getRed() * baseWeight);
        int green = (int) (lightColor.getGreen() * lightWeight + baseColor.getGreen() * baseWeight);
        int blue = (int) (lightColor.getBlue() * lightWeight + baseColor.getBlue() * baseWeight);
        int alpha = Math.max(lightColor.getAlpha(), baseColor.getAlpha());
        
        return TileColor.defaultForegroundColor()
                .withRed(red)
                .withGreen(green)  
                .withBlue(blue)
                .withAlpha(alpha);
    }
}
