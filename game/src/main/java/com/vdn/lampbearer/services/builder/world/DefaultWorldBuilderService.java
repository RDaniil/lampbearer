package com.vdn.lampbearer.services.builder.world;

import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.world.World;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.prefab.Prefab;
import com.vdn.lampbearer.prefab.PrefabData;
import com.vdn.lampbearer.prefab.PrefabReader;
import com.vdn.lampbearer.services.RandomService;
import com.vdn.lampbearer.services.config.ConfigFileParser;
import com.vdn.lampbearer.services.config.TileGameBlockConfig;
import com.vdn.lampbearer.services.interfaces.WorldBuilderService;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import lombok.RequiredArgsConstructor;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.vdn.lampbearer.factories.GameBlockFactory.returnGameBlock;

@Service
@RequiredArgsConstructor
public class DefaultWorldBuilderService implements WorldBuilderService {
    private final HashMap<Position3D, GameBlock> blocks = new HashMap<>();


    @Override
    public World buildWorld(Size3D worldSize, Size3D visibleSize) {
        TileGameBlockConfig tileGameBlockConfig;
        try {
            tileGameBlockConfig = ConfigFileParser.parseConfigFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TileRepository.fillTileMap(tileGameBlockConfig);
        GameBlockFactory.fillGameBlockMap(tileGameBlockConfig);

        Iterator<Position3D> it = worldSize.fetchPositions().iterator();
        while (it.hasNext()) {
            Position3D pos = it.next();

            if (RandomService.getRandom(0, 20) % 4 == 0) {
                blocks.put(pos, returnGameBlock(BlockType.GRASS));
            } else if (RandomService.getRandom(0, 30) % 29 == 0) {
                blocks.put(pos, returnGameBlock(BlockType.ROCK));
            } else {
                blocks.put(pos, returnGameBlock(BlockType.GROUND));
            }
        }

        Map<Position3D, GameBlock> building;
        try {
            building = getBuilding(Position3D.create(10, 10, 0));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        blocks.putAll(building);
        return new World(visibleSize, worldSize, blocks);
    }


    public Map<Position3D, GameBlock> getBuilding(Position3D offset) throws FileNotFoundException {

        PrefabData prefabData = PrefabReader.read(Prefab.SMALL_BUILDING);

        Map<Position3D, GameBlock> positionToBlockMap = new HashMap<>();

        for (var positionToBlock : prefabData.getBlockMap().entrySet()) {
            positionToBlockMap.put(
                    positionToBlock.getKey().withRelative(offset),
                    positionToBlock.getValue()
            );
        }

        return positionToBlockMap;
    }
}
