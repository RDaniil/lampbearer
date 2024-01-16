package com.vdn.lampbearer.services.light;

import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.utils.PositionUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Tile;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public abstract class Light {
    private Position position;
    private final int radius;
    private final TileColor color;


    /**
     * Вычисляет цвет конкретного блока, освещенного этим светом
     *
     * @param block         блок, который надо осветить
     * @param blockPosition позиция блока, который надо осветить
     * @return цвет освещенного блока
     */
    @Nullable
    public TileColor getColorOfTile(GameBlock block, Position blockPosition) {
        double distance = PositionUtils.getDistance(position, blockPosition);
        if (block.hasEntities()) {
            return getColorForEntity(distance, block);
        } else {
            return getColorForBlock(distance, block);
        }
    }


    /**
     * Вычисляет цвет тайла сущности, нужно потому что цвет будет переходить в цвет
     * источника света. И в близи не понятно какого цвета монстр перед тобой находится
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
        return displayedTile.getForegroundColor().darkenByPercent(brightness);
    }


    private TileColor getColorForBlock(double distance, GameBlock block) {
        TileColor foregroundColor = block.getContent().getForegroundColor();

        if (distance > radius) {
            return foregroundColor;
        }

        float brightness = (float) (distance / radius);
        var colorInterpolator = color.interpolateTo(foregroundColor);
        return colorInterpolator.getColorAtRatio(brightness);
    }
}