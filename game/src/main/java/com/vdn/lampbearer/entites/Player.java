package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.entites.interfaces.AbstractEntity;
import com.vdn.lampbearer.views.TileRepository;

public class Player extends AbstractEntity {
    public Player() {
        setTile(TileRepository.PLAYER);
    }
}
