package com.vdn.lampbearer.services.builder.world;

import com.vdn.lampbearer.config.LevelsConfig;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.objects.LampPost;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.world.World;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.config.ConfigFileParser;
import com.vdn.lampbearer.services.config.TileGameBlockConfig;
import com.vdn.lampbearer.services.interfaces.WorldBuilderService;
import com.vdn.lampbearer.views.TileRepository;
import lombok.RequiredArgsConstructor;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;



import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Chizhov D. on 2024.03.09
 */

@RequiredArgsConstructor
public class SimpleWorldBuilderService implements WorldBuilderService {

    @Override
    public World buildWorld(Size3D worldSize, Size3D visibleSize) {
        try {
            TileGameBlockConfig tileGameBlockConfig = ConfigFileParser.parseConfigFile();
            TileRepository.fillTileMap(tileGameBlockConfig);
            GameBlockFactory.fillGameBlockMap(tileGameBlockConfig);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<Position3D, GameBlock> positionToBlockMap;

        try {
            LevelsConfig levelsConfig = ConfigFileParser.parseLevelsConfig();
            positionToBlockMap = levelsConfig.getMap(0, worldSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        World world = new World(visibleSize, worldSize, positionToBlockMap);

        List<AbstractEntity> lampposts = positionToBlockMap.values().stream()
                .filter(GameBlock::hasEntities).map(GameBlock::getEntities).flatMap(List::stream)
                .filter(e -> e instanceof LampPost).collect(Collectors.toList());

        lampposts.forEach(lp -> world.addStaticLight(((LampPost) lp).getLight()));

        return world;
    }
}
