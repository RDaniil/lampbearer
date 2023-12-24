package com.vdn.lampbearer.attributes.arrangement;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VerticalArrangement implements Arrangement {

    private static VerticalArrangement INSTANCE;


    public static VerticalArrangement getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new VerticalArrangement());
    }
}
