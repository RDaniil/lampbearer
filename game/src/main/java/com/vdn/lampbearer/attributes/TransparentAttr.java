package com.vdn.lampbearer.attributes;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Chizhov D. on 2024.03.08
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransparentAttr implements Attribute {

    private static TransparentAttr INSTANCE;


    public static TransparentAttr getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new TransparentAttr());
    }
}
