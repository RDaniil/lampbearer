package com.vdn.lampbearer.attributes.items;

import com.vdn.lampbearer.attributes.Attribute;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FueledByOilAttr implements Attribute {

    private static FueledByOilAttr INSTANCE;


    public static FueledByOilAttr getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new FueledByOilAttr());
    }
}
