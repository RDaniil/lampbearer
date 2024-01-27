package com.vdn.lampbearer.attributes;

import com.vdn.lampbearer.services.light.Light;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class LightSourceAttr implements Attribute {
    private boolean isOn = false;
    private final Light light;
}
