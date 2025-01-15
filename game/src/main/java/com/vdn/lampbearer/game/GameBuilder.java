package com.vdn.lampbearer.game;

import com.vdn.lampbearer.attributes.creature.SpeedAttr;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.entites.SimpleZombie;
import com.vdn.lampbearer.entites.item.FirstAidKit;
import com.vdn.lampbearer.entites.item.Lantern;
import com.vdn.lampbearer.entites.item.LighthouseLamp;
import com.vdn.lampbearer.entites.item.OilBottle;
import com.vdn.lampbearer.entites.item.firearm.Revolver;
import com.vdn.lampbearer.entites.item.projectile.ammo.FMJRevolverAmmoBox;
import com.vdn.lampbearer.entites.objects.LampPost;
import com.vdn.lampbearer.game.world.World;
import com.vdn.lampbearer.services.interfaces.WorldBuilderService;
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

        LighthouseLamp lamp = (LighthouseLamp) world.findEntityByType(LighthouseLamp.class);

        Player player = new Player(lamp.getPosition());
        world.scrollUpBy(player.getPosition().getX());

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

        LampPost lampPost = new LampPost(Position3D.create(13, 13, 0));
        world.addEntity(lampPost, lampPost.getPosition());
        world.addStaticLight(lampPost.getLight());

        world.addEntity(FirstAidKit.createForWorld(Position3D.create(5, 4, 0)), Position3D.create(5, 4, 0));
        world.addEntity(Revolver.createForWorld(Position3D.create(26, 25, 0)), Position3D.create(26, 25, 0));
        world.addEntity(FMJRevolverAmmoBox.createForWorld(Position3D.create(26, 26, 0)), Position3D.create(26, 26, 0));
        world.addEntity(new Lantern(Position3D.create(22, 22, 0)), Position3D.create(22, 22, 0));
        world.addEntity(new OilBottle(Position3D.create(23, 23, 0)), Position3D.create(23, 23, 0));
        return new Game(world, player);
    }
}
