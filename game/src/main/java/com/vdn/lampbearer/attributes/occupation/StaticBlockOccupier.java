package com.vdn.lampbearer.attributes.occupation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A BlockOccupier which can't be removed by anybody's action
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StaticBlockOccupier implements BlockOccupier {

    private static StaticBlockOccupier INSTANCE;


    public static StaticBlockOccupier getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new StaticBlockOccupier());
    }
}
