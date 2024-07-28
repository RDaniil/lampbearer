package com.vdn.lampbearer.prefab;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.exception.NoTileFoundException;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.views.TileRepository;
import org.hexworks.zircon.api.data.CharacterTile;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.resource.REXPaintResources;
import org.hexworks.zircon.internal.resource.REXPaintResource;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.vdn.lampbearer.factories.GameBlockFactory.returnGameBlock;
import static com.vdn.lampbearer.views.TileRepository.getBlockType;

/**
 * @author Chizhov D. on 2024.03.09
 */
public class PrefabReader {

    @NotNull
    public static PrefabData read(Prefab prefab) {
        REXPaintResource rex;

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream stream = classloader.getResourceAsStream(prefab.getPathToFile())) {
            rex = REXPaintResources.loadREXFile(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Layer> layers = rex.toLayerList();
        Map<Position, Tile> tileMap = layers.get(0).getTiles();

        Map<Position, Tile> entityMap = Collections.emptyMap();
        if (layers.size() > 1) {
            entityMap = layers.get(1).getTiles();
        }

        Set<CharacterTile> notFoundTiles = new HashSet<>();

        Map<Position3D, GameBlock> positionToBlockMap = new HashMap<>();
        for (Map.Entry<Position, Tile> positionToTile : tileMap.entrySet()) {
            Position position = positionToTile.getKey();
            Position3D position3D = position.toPosition3D(0);
            GameBlock block;
            try {
                block = returnGameBlock(TileRepository.getTile(positionToTile.getValue()));

                Tile entityTile = entityMap.get(position);
                if (entityTile != null) {
                    block.addEntity(AbstractEntity.create(getBlockType(entityTile), position3D));
                }
            } catch (NoTileFoundException e) {
                notFoundTiles.add(e.getTile());
                continue;
            }

            positionToBlockMap.put(position3D, block);
        }

        if (!notFoundTiles.isEmpty()) {
            throw new NoTileFoundException(notFoundTiles, prefab.getPathToFile());
        }
        return new PrefabData(prefab, positionToBlockMap);
    }
}
