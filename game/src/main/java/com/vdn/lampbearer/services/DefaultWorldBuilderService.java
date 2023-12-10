package com.vdn.lampbearer.services;

import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.GameBlock;
import com.vdn.lampbearer.game.World;
import com.vdn.lampbearer.services.interfaces.WorldBuilderService;
import lombok.RequiredArgsConstructor;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;

@Service
@RequiredArgsConstructor
public class DefaultWorldBuilderService implements WorldBuilderService {

    private final GameBlockFactory gameBlockFactory;
    private final HashMap<Position3D, GameBlock> blocks = new HashMap<>();

    public World buildWorld(Size3D worldSize, Size3D visibleSize) {
        Iterator<Position3D> it = worldSize.fetchPositions().iterator();
        while (it.hasNext()) {
            Position3D pos = it.next();
            if (RandomService.getRandom(0, 10) % 7 == 0) {
                blocks.put(pos, gameBlockFactory.createGrass());
            } else {
                blocks.put(pos, gameBlockFactory.createGround());
            }
        }
        return new World(visibleSize, worldSize, blocks);
    }
}
