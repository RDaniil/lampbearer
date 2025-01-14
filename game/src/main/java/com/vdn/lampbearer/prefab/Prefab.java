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
    SMALL_BUILDING_BROKEN("prefabs/SmallBuildingBroken.xp"),
    MEDIUM_BUILDING("prefabs/MediumBuilding.xp"),
    MEDIUM_BUILDING_BROKEN("prefabs/MediumBuildingBroken.xp"),
    STORAGE("prefabs/Storage.xp"),
    SHOP("prefabs/Shop.xp"),
    HOSPITAL("prefabs/Hospital.xp"),
    LIGHTHOUSE("prefabs/Lighthouse.xp"),
    LAMPBEARER_BUILDING("prefabs/LampBearerBuilding.xp"),
    ;

    private final String pathToFile;
}
