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

        float brightness = (float) (distance / radius) - 0.3f;
        if (brightness < 0) brightness = 0;
        var colorInterpolator = color.interpolateTo(displayedTile.getForegroundColor());
        /*При нахождении монстра в упор не нужно окрашивать
        его полностью в цвет света*/
        TileColor halfLightedEntityTile = colorInterpolator.getColorAtRatio(0.6);

        colorInterpolator = halfLightedEntityTile
                .interpolateTo(displayedTile.getForegroundColor());
        return colorInterpolator.getColorAtRatio(brightness);
    }


    private TileColor getColorForBlock(double distance, GameBlock block) {
        TileColor foregroundColor = block.getDisplayedTile().getForegroundColor();

        if (distance > radius) {
            return foregroundColor;
        }

        float brightness = (float) (distance / radius);
        var colorInterpolator = color.interpolateTo(foregroundColor);
        return colorInterpolator.getColorAtRatio(brightness);
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
