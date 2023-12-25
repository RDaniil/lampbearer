package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.action.Action;
import com.vdn.lampbearer.attributes.Attribute;
import lombok.Getter;
import lombok.Setter;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public abstract class AbstractEntity {
    private String name;
    private String description;
    private Tile tile;
    protected Position3D position;
    private List<Attribute> attributes = new ArrayList<>();
    private List<Action<?>> actions = new ArrayList<>();


    public <T extends Attribute> Optional<T> findAttribute(Class<T> attributeType) {
        return attributes.stream()
                .filter(attributeType::isInstance)
                .map(a -> (T) a)
                .findFirst();
    }


    public <T extends Attribute> void removeAttribute(Class<T> attributeType) {
        attributes.removeIf(attributeType::isInstance);
    }


    public void setAttributes(List<Attribute> attributes) {
        this.attributes.clear();
        this.attributes.addAll(attributes);
    }


    public <T extends Action<?>> Optional<T> findAction(Class<T> actionType) {
        return actions.stream()
                .filter(actionType::isInstance)
                .map(a -> (T) a)
                .findFirst();
    }


    public <T extends Action<?>> void removeAction(Class<T> actionType) {
        actions.removeIf(actionType::isInstance);
    }


    public void setActions(List<Action<?>> actions) {
        this.actions.clear();
        this.actions.addAll(actions);
    }
}
