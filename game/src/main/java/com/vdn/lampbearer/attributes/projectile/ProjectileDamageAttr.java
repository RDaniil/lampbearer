package com.vdn.lampbearer.attributes.projectile;

import com.vdn.lampbearer.attributes.Attribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProjectileDamageAttr implements Attribute {
    private int value;
}
