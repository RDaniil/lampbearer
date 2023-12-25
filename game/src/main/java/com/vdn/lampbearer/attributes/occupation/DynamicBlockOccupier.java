package com.vdn.lampbearer.attributes.occupation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A BlockOccupier which can be removed by somebody's action
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DynamicBlockOccupier implements BlockOccupier {

    private static DynamicBlockOccupier INSTANCE;


    public static DynamicBlockOccupier getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new DynamicBlockOccupier());
    }
}
