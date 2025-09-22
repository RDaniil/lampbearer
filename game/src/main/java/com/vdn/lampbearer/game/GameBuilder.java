package com.vdn.lampbearer.game;

import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.entites.item.*;
import com.vdn.lampbearer.game.world.World;
import com.vdn.lampbearer.services.builder.world.WorldBuilderService;
import lombok.RequiredArgsConstructor;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;


import static com.vdn.lampbearer.config.GameConfig.*;

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

        BrokenLighthouseKey key = (BrokenLighthouseKey) world.findEntityByType(BrokenLighthouseKey.class);

        Player player = new Player(key.getPosition().plus(Position3D.create(1,0,0)));
        world.scrollUpBy(player.getPosition().getX());

        world.addEntity(player, player.getPosition());
        world.addDynamicLight(player, player.getFowLight());

//        Lantern lantern = (Lantern) world.findEntityByType(Lantern.class);
//        new LightLampReaction().execute(lantern, lantern, tmpContext);
//           world.addEntity(LighthouseLampSocket.createForWorld(player.getPosition()), player.getPosition());
//        // Create 3 intersecting circle lights for testing color blending
//        // Cyan light at (28, 30)
//        CircleLight cyanLight = new CircleLight(Position.create(28, 30), 7, TileColor.fromString("#00FFFF"));
//        world.addStaticLight(cyanLight);
//
//        // Magenta light at (32, 30)
//        CircleLight magentaLight = new CircleLight(Position.create(32, 30), 7, TileColor.fromString("#FF00FF"));
//        world.addStaticLight(magentaLight);
//
//        // Yellow light at (30, 27)
//        CircleLight yellowLight = new CircleLight(Position.create(30, 27), 7, TileColor.fromString("#FFFF00"));
//        world.addStaticLight(yellowLight);

        return new Game(world, player);
    }
}
