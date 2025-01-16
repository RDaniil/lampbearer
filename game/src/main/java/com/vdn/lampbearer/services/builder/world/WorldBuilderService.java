package com.vdn.lampbearer.services.builder.world;

import com.vdn.lampbearer.game.world.World;
import org.hexworks.zircon.api.data.Size3D;

public interface WorldBuilderService {
    World buildWorld(Size3D worldSize, Size3D visibleSize);
}
