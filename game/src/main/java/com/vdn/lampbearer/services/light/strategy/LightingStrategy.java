package com.vdn.lampbearer.services.light.strategy;

import com.vdn.lampbearer.services.light.Light;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;

import java.util.HashMap;

public interface LightingStrategy {
    /**
     * Высчитывает цвет блоков
     *
     * @param light источник света, для которого происходят вычисления
     * @return HashMap: позиция-цвет освещенного тайла
     */
    HashMap<Position, TileColor> lightBlocks(Light light);
}
