package com.vdn.lampbearer.attributes;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A flag which tells us that block is occupied by something/someone
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockOccupier implements Attribute {

    private static BlockOccupier INSTANCE;


    public static BlockOccupier getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new BlockOccupier());
    }
}
