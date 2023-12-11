package com.vdn.lampbearer.game;

import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.game.world.World;
import lombok.Getter;

@Getter
public class Game {

    private final World world;
    private final Player player;

    public Game(World world, Player player) {
        this.world = world;
        this.player = player;
    }
}
