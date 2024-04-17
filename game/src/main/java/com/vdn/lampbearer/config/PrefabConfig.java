package com.vdn.lampbearer.config;

import com.vdn.lampbearer.prefab.Prefab;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author Chizhov D. on 2024.03.14
 */
@Getter
@AllArgsConstructor
public class PrefabConfig {

    private final Prefab prefab;
    private final Integer number;

    @Accessors(fluent = true)
    private final boolean shouldBeConnectedByTrails;
}
