package com.vdn.lampbearer.game;

import com.vdn.lampbearer.attributes.SpeedAttr;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.entites.SimpleZombie;
import com.vdn.lampbearer.game.world.World;
import com.vdn.lampbearer.services.interfaces.WorldBuilderService;
import com.vdn.lampbearer.services.light.CircleLight;
import lombok.RequiredArgsConstructor;
import org.hexworks.zircon.api.color.TileColor;
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
        Player player = new Player(Position3D.create(25, 25, 0));
        world.addEntity(player, player.getPosition());
        world.addDynamicLight(player, player.getFowLight());

        SimpleZombie zombie = new SimpleZombie(Position3D.create(5, 6, 0));
        zombie.removeAttribute(SpeedAttr.class);
        zombie.getAttributes().add(new SpeedAttr(10));
        world.addEntity(zombie, zombie.getPosition());

        zombie = new SimpleZombie(Position3D.create(5, 7, 0));
        zombie.removeAttribute(SpeedAttr.class);
        zombie.getAttributes().add(new SpeedAttr(14));
        world.addEntity(zombie, zombie.getPosition());

        zombie = new SimpleZombie(Position3D.create(5, 4, 0));
        world.addEntity(zombie, zombie.getPosition());
        CircleLight zombieLight = new CircleLight(zombie.getPosition(), 4, TileColor.fromString("#FF4200"));
        world.addDynamicLight(zombie, zombieLight);

        return new Game(world, player);
    }
}
