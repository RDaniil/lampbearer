package com.vdn.lampbearer.prefab;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Chizhov D. on 2024.03.09
 */
@Getter
@AllArgsConstructor
public enum Prefab {

    SMALL_BUILDING("prefabs/SmallBuilding.xp"),
    ;

    private final String pathToFile;
}
