package com.vdn.lampbearer.game;

import com.vdn.lampbearer.action.reactions.items.LightLampReaction;
import com.vdn.lampbearer.attributes.creature.SpeedAttr;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.entites.SimpleZombie;
import com.vdn.lampbearer.entites.item.*;
import com.vdn.lampbearer.entites.item.firearm.Revolver;
import com.vdn.lampbearer.entites.item.projectile.ammo.FMJRevolverAmmoBox;
import com.vdn.lampbearer.entites.objects.LampPost;
import com.vdn.lampbearer.game.world.World;
import com.vdn.lampbearer.services.builder.world.WorldBuilderService;
import com.vdn.lampbearer.services.light.CircleLight;
import lombok.RequiredArgsConstructor;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
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

        BrokenLighthouseKey lamp = (BrokenLighthouseKey) world.findEntityByType(BrokenLighthouseKey.class);

        Player player = new Player(lamp.getPosition().plus(Position3D.create(1,0,0)));
        world.scrollUpBy(player.getPosition().getX());

        world.addEntity(player, player.getPosition());
        world.addDynamicLight(player, player.getFowLight());

        Lantern lantern = (Lantern) world.findEntityByType(Lantern.class);
        GameContext tmpContext = new GameContext();
        tmpContext.setWorld(world);

        new LightLampReaction().execute(lantern, lantern, tmpContext);

        SimpleZombie zombie = new SimpleZombie(Position3D.create(5, 6, 0));
        zombie.removeAttribute(SpeedAttr.class);
        zombie.getAttributes().add(new SpeedAttr(10));
        world.addEntity(zombie, zombie.getPosition());

        zombie = new SimpleZombie(Position3D.create(5, 7, 0));
        zombie.removeAttribute(SpeedAttr.class);
        zombie.getAttributes().add(new SpeedAttr(14));
        world.addEntity(zombie, zombie.getPosition());

        LampPost lampPost = new LampPost(Position3D.create(13, 13, 0));
        world.addEntity(lampPost, lampPost.getPosition());
        world.addStaticLight(lampPost.getLight());

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

        world.addEntity(FirstAidKit.createForWorld(Position3D.create(5, 4, 0)), Position3D.create(5, 4, 0));
        world.addEntity(Revolver.createForWorld(Position3D.create(26, 25, 0)), Position3D.create(26, 25, 0));
        world.addEntity(FMJRevolverAmmoBox.createForWorld(Position3D.create(26, 26, 0)), Position3D.create(26, 26, 0));
        world.addEntity(new Lantern(Position3D.create(22, 22, 0)), Position3D.create(22, 22, 0));
        world.addEntity(new OilBottle(Position3D.create(23, 23, 0)), Position3D.create(23, 23, 0));
        return new Game(world, player);
    }
}
