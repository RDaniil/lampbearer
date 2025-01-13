package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.action.actions.Action;
import com.vdn.lampbearer.attributes.Attribute;
import com.vdn.lampbearer.attributes.TransparentAttr;
import com.vdn.lampbearer.attributes.arrangement.HorizontalArrangement;
import com.vdn.lampbearer.attributes.arrangement.VerticalArrangement;
import com.vdn.lampbearer.attributes.occupation.BlockOccupier;
import com.vdn.lampbearer.entites.item.FirstAidKit;
import com.vdn.lampbearer.entites.item.Lantern;
import com.vdn.lampbearer.entites.item.OilBottle;
import com.vdn.lampbearer.entites.item.projectile.ammo.FMJRevolverAmmoBox;
import com.vdn.lampbearer.entites.item.projectile.revolver.DefaultRevolverRound;
import com.vdn.lampbearer.entites.item.projectile.revolver.SignalRevolverRound;
import com.vdn.lampbearer.entites.objects.Door;
import com.vdn.lampbearer.entites.objects.LampPost;
import com.vdn.lampbearer.views.BlockType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Tile;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
public abstract class AbstractEntity implements Serializable {

    protected transient Position3D position;
    private String name = StringUtils.EMPTY;
    private String description = StringUtils.EMPTY;
    private transient Tile tile = Tile.empty();
    private Set<Attribute> attributes = new HashSet<>();
    private Set<Action<?>> actions = new HashSet<>();


    protected AbstractEntity(Position3D position) {
        this.position = position;
    }


    @SuppressWarnings({"unchecked"})
    public <T extends Attribute> Optional<T> findAttribute(Class<T> attributeType) {
        return attributes.stream()
                .filter(attributeType::isInstance)
                .map(a -> (T) a)
                .findFirst();
    }


    public <T extends Attribute> boolean hasAttribute(Class<T> attributeType) {
        return attributes.stream()
                .anyMatch(attributeType::isInstance);
    }


    public <T extends Attribute> void removeAttribute(Class<T> attributeType) {
        attributes.removeIf(attributeType::isInstance);
    }


    public void setAttributes(List<Attribute> attributes) {
        this.attributes.clear();
        this.attributes.addAll(attributes);
    }


    @SuppressWarnings({"unchecked"})
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


    public boolean isTransparent() {
        return findAttribute(TransparentAttr.class).isPresent() ||
                findAttribute(BlockOccupier.class).isEmpty();
    }


    @Override
    @SuppressWarnings({"all"})
    public AbstractEntity clone() {
        AbstractEntity clone = SerializationUtils.clone(this);
        clone.position = position;
        clone.tile = tile;
        return clone;
    }


    @NotNull
    public static AbstractEntity create(@NotNull BlockType blockType,
                                        @NotNull Position3D position) {
        switch (blockType) {
            case FIRST_AID_KIT:
                return FirstAidKit.createForWorld(position);
            case LAMPPOST:
                return new LampPost(position);
            case LANTERN:
                return new Lantern(position);
            case H_CLOSED_DOOR:
                return new Door(position, HorizontalArrangement.getInstance());
            case OIL_BOTTLE:
                return new OilBottle(position);
            case OPENED_DOOR:
                return new Door(position);
            case PLAYER:
                return new Player(position);
            case SIMPLE_ZOMBIE:
                return new SimpleZombie(position);
            case SWARMER:
                return new Swarmer(position);
            case V_CLOSED_DOOR:
                return new Door(position, VerticalArrangement.getInstance());
            case DEFAULT_REVOLVER_ROUND:
                return new DefaultRevolverRound(position);
            case DEFAULT_REVOLVER_ROUND_BOX:
                return FMJRevolverAmmoBox.createForWorld(position);
            case SIGNAL_REVOLVER_ROUND:
                return SignalRevolverRound.createForWorld(position);
            default:
                throw new RuntimeException("Not implemented yet!");
        }
    }
}
