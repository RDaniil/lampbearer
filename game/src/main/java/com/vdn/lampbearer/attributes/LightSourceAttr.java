package com.vdn.lampbearer.attributes;

import com.vdn.lampbearer.services.light.Light;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LightSourceAttr implements Attribute {
    private Light light;
}
