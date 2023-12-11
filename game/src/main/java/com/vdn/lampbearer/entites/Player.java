package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.entites.interfaces.AbstractEntity;
import com.vdn.lampbearer.entites.interfaces.Schedulable;
import com.vdn.lampbearer.views.TileRepository;

public class Player extends AbstractEntity implements Schedulable {
    public Player() {
        setTile(TileRepository.PLAYER);
    }

    @Override
    public int getTime() {
        return 5;
    }
}
