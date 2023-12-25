package com.vdn.lampbearer.attributes.arrangement;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HorizontalArrangement implements Arrangement {

    private static HorizontalArrangement INSTANCE;


    public static HorizontalArrangement getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new HorizontalArrangement());
    }
}
