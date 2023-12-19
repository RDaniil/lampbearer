package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.attributes.Attribute;
import lombok.Getter;
import lombok.Setter;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Tile;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
public abstract class AbstractEntity {
    private Tile tile;
    private Position3D position;
    private List<Attribute> attributes;

    public <T extends Attribute> Optional<T> findAttribute(Class<T> attributeType) {
        return attributes.stream()
                .filter(attributeType::isInstance)
                .map(a -> (T) a)
                .findFirst();
    }
}
