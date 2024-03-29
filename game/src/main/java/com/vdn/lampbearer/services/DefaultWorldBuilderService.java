package com.vdn.lampbearer.services;

import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.world.World;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.config.ConfigFileParser;
import com.vdn.lampbearer.services.config.TileGameBlockConfig;
import com.vdn.lampbearer.services.interfaces.WorldBuilderService;
import com.vdn.lampbearer.views.BlockTypes;
import com.vdn.lampbearer.views.TileRepository;
import lombok.RequiredArgsConstructor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.resource.REXPaintResources;
import org.hexworks.zircon.internal.resource.REXPaintResource;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DefaultWorldBuilderService implements WorldBuilderService {
    private final HashMap<Position3D, GameBlock> blocks = new HashMap<>();


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
                blocks.put(pos, GameBlockFactory.returnGameBlock(BlockTypes.GRASS));
            } else if (RandomService.getRandom(0, 30) % 29 == 0) {
                blocks.put(pos, GameBlockFactory.returnGameBlock(BlockTypes.ROCK));
            } else {
                blocks.put(pos, GameBlockFactory.returnGameBlock(BlockTypes.GROUND));
            }
        }

        Map<Position3D, GameBlock> building = null;
        try {
            building = getBuilding(Position3D.create(10, 10, 0));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        blocks.putAll(building);
        return new World(visibleSize, worldSize, blocks);
    }


    public Map<Position3D, GameBlock> getBuilding(Position3D offset) throws FileNotFoundException {
        File simpleBuildingFile = Paths.get("game", "src", "main",
                "resources", "prefabs", "SmallBuilding.xp").toFile();
        Map<Position, Tile> tiles = getBlocks(simpleBuildingFile);

        var positionToBlock = new HashMap<Position3D, GameBlock>();

        for (Map.Entry<Position, Tile> positionTileEntry : tiles.entrySet()) {
            positionToBlock.put(positionTileEntry.getKey().toPosition3D(0).withRelative(offset),
                    getBlockByTile(positionTileEntry.getValue()));
        }

        return positionToBlock;
    }


    @NotNull
    private static Map<Position, Tile> getBlocks(File file) {
        REXPaintResource rex;
        try (FileInputStream inputStream = new FileInputStream(file)) {
            rex = REXPaintResources.loadREXFile(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<Position, Tile> positionTileMap = rex.toLayerList().get(0).getTiles();
        HashMap<Position, Tile> positionTileHashMap = new HashMap<>(positionTileMap);
        for (Map.Entry<Position, Tile> positionTileEntry : positionTileHashMap.entrySet()) {
            positionTileEntry.setValue(TileRepository.getTile(positionTileEntry.getValue()));
        }

        return positionTileMap;
    }


    private GameBlock getBlockByTile(Tile tile) {
        return GameBlockFactory.returnGameBlock(TileRepository.getBlockType(tile));
    }
}
