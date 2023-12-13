package com.vdn.lampbearer.entites;

import lombok.Getter;
import lombok.Setter;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Tile;

@Getter
@Setter
public abstract class AbstractEntity {
    private Tile tile;
    private Position3D position;
}
