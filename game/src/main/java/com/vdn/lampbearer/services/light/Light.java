package com.vdn.lampbearer.services.light;

import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.utils.PositionUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Tile;

import java.io.Serializable;

/**
 * Общий класс для представления источников освещения.
 * ВАЖНО: Если внутри сущности хранится наследник этого класса, необходимо переопределить метод clone()
 * Для корректного копирования TileColor color
 */
@Getter
@Setter
public abstract class Light implements Serializable {

    private transient Position position;
    protected PositionUtils.Direction direction = PositionUtils.Direction.RIGHT;
    private int radius;

    @Setter(AccessLevel.NONE)
    private transient TileColor color;


    public Light(Position position, int radius, TileColor color) {
        this.position = position;
        this.radius = radius;
        this.color = color;
    }


    /**
     * Вычисляет цвет конкретного блока, освещенного этим светом
     *
     * @param block         блок, который надо осветить
     * @param blockPosition позиция блока, который надо осветить
     * @return цвет освещенного блока
     */
    public TileColor getColorOfTile(GameBlock block, Position blockPosition) {
        double distance = PositionUtils.getDistance(position, blockPosition);
        if (block.hasEntities()) {
            return getColorForEntity(distance, block);
        } else {
            return getColorForBlock(distance, block);
        }
    }


    /**
     * Вычисляет цвет тайла сущности. От освещения блока отличается тем, что блок можно полностью
     * окрасить в цвет света, а сущность красим только частично, чтобы читался ее исходный цвет.
     * И в близи было понятно на кого мы смотрим
     *
     * @param distance дистанция до сущности
     * @param block    блок на котором стоит монстр
     * @return освещенный цвет монстра
     */
    private TileColor getColorForEntity(double distance, GameBlock block) {
        Tile displayedTile = block.getDisplayedTile();
        if (distance > radius) {
            return displayedTile.getForegroundColor();
        }

        // Calculate light intensity with smoother falloff
        float normalizedDistance = (float) (distance / radius); // 0 to 1
        float intensity = 1.0f - (normalizedDistance * normalizedDistance); // Quadratic falloff
        intensity = Math.max(0.0f, Math.min(1.0f, intensity));
        
        // Reduce intensity for entities to maintain readability
        intensity *= 0.4f; // Max 40% light effect on entities
        
        // Blend colors based on distance
        return blendLightWithBase(color, displayedTile.getForegroundColor(), intensity);
    }


    private TileColor getColorForBlock(double distance, GameBlock block) {
        TileColor foregroundColor = block.getDisplayedTile().getForegroundColor();

        if (distance > radius) {
            return foregroundColor;
        }

        // Calculate light intensity with smoother falloff
        float normalizedDistance = (float) (distance / radius); // 0 to 1
        float intensity = 1.0f - (normalizedDistance * normalizedDistance); // Quadratic falloff for smoother transition
        intensity = Math.max(0.0f, Math.min(1.0f, intensity));
        
        // For very close distances, ensure some base color remains visible
        if (distance < 1.0) {
            intensity = Math.min(intensity, 0.8f);
        }
        
        // Blend colors based on distance
        return blendLightWithBase(color, foregroundColor, intensity);
    }
    
    /**
     * Blends light color with base color based on intensity
     * @param lightColor The light color
     * @param baseColor The base/original color
     * @param intensity The light intensity (0.0 = no light, 1.0 = full light)
     * @return The blended color
     */
    private TileColor blendLightWithBase(TileColor lightColor, TileColor baseColor, float intensity) {
        intensity = Math.max(0, Math.min(1, intensity)); // Clamp between 0 and 1
        
        // Linear interpolation between base color and light color
        int red = (int) (baseColor.getRed() * (1.0f - intensity) + lightColor.getRed() * intensity);
        int green = (int) (baseColor.getGreen() * (1.0f - intensity) + lightColor.getGreen() * intensity);
        int blue = (int) (baseColor.getBlue() * (1.0f - intensity) + lightColor.getBlue() * intensity);
        
        return TileColor.defaultForegroundColor()
                .withRed(red)
                .withGreen(green)
                .withBlue(blue);
    }


    @Override
    @SuppressWarnings({"all"})
    public Light clone() {
        Light clone = SerializationUtils.clone(this);
        clone.position = position;
        clone.color = color;
        return clone;
    }
}
