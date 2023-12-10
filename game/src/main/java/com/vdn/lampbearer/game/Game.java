package com.vdn.lampbearer.game;

import com.vdn.lampbearer.entites.Player;
import lombok.Getter;

@Getter
public class Game {

    public final World world;
    public final Player player;

    public Game(World world, Player player) {
        this.world = world;
        this.player = player;
    }
}
