package com.vdn.lampbearer.attributes;

import com.vdn.lampbearer.services.light.Light;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;

@Getter
@Setter
public class LightSourceAttr implements Attribute {

    private boolean isOn = false;

    @Setter(AccessLevel.NONE)
    private Light light;


    public LightSourceAttr(Light light) {
        this.light = light;
    }


    @Override
    @SuppressWarnings({"all"})
    public LightSourceAttr clone() {
        LightSourceAttr clone = SerializationUtils.clone(this);
        clone.light = light.clone();
        return clone;
    }
}
