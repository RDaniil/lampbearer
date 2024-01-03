package com.vdn.lampbearer.game;

import com.vdn.lampbearer.attributes.SpeedAttr;
import com.vdn.lampbearer.attributes.arrangement.VerticalArrangement;
import com.vdn.lampbearer.entites.Door;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.entites.SimpleZombie;
import com.vdn.lampbearer.game.world.World;
import com.vdn.lampbearer.services.interfaces.WorldBuilderService;
import lombok.RequiredArgsConstructor;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;
import org.springframework.stereotype.Component;

import static com.vdn.lampbearer.config.GameConfig.*;

@Component
@RequiredArgsConstructor
public class GameBuilder {

    private final WorldBuilderService worldBuilderService;

    private final Size3D worldVisibleSize = Size3D.create(
            WINDOW_WIDTH - SIDEBAR_WIDTH,
            WINDOW_HEIGHT - LOG_AREA_HEIGHT,
            1
    );


    public Game buildGame(Size3D worldSize) {
        World world = worldBuilderService.buildWorld(worldSize, worldVisibleSize);
        Player player = new Player();
        world.addEntity(player, Position3D.create(5, 5, 0));

        world.addEntity(new SimpleZombie(new SpeedAttr(10)), Position3D.create(5, 6, 0));
        world.addEntity(new SimpleZombie(new SpeedAttr(14)), Position3D.create(5, 7, 0));
        world.addEntity(new SimpleZombie(new SpeedAttr(3)), Position3D.create(5, 4, 0));
        world.addEntity(new Door(VerticalArrangement.getInstance()), Position3D.create(10, 12, 0));

        return new Game(world, player);
    }
}
