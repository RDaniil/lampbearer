package com.vdn.lampbearer.config;

import com.vdn.lampbearer.prefab.Prefab;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Chizhov D. on 2024.03.14
 */
@Getter
@AllArgsConstructor
public class PrefabConfig {

    private final Prefab prefab;
    private final Integer number;
    private final boolean shouldSpawnEverything;
    /**
     * Спавнить ентити на тех местах на которых они указаны в префабе
     */
    private final boolean staticEntities;
    /**
     * Промежуток по оси Х на которой можно спавнить префаб (в процентах от ширины карты от 0.0 до 1.0)
     */
    private final List<Float> xSpawnRange;
    private final List<Float> ySpawnRange;

    @Accessors(fluent = true)
    private final boolean shouldBeConnectedByTrails;
}
