package com.vdn.lampbearer.services.light;

import com.vdn.lampbearer.attributes.creature.PerceptionAttr;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Position3D;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Chizhov D. on 2024.03.03
 */
public class PlayerSight extends CircleLight {

    private final Set<Position3D> positions = new HashSet<>();


    public PlayerSight(PerceptionAttr perceptionAttr) {
        super(
                Position.create(0, 0),
                perceptionAttr.getValue(),
                TileColor.fromString("#000000")
        );
    }


    public void reset(Set<Position> positions) {
        this.positions.clear();
        positions.forEach(p -> this.positions.add(p.toPosition3D(0)));
    }


    public boolean contains(Position3D position) {
        return positions.contains(position);
    }
}
