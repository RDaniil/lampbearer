package com.vdn.lampbearer.factories;

import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.views.TileRepository;
import org.springframework.stereotype.Service;

/**
 * Фабрика игровых блоков
 */
@Service
public class GameBlockFactory {
    public GameBlock createGround() {
        //TODO енам с типами блоков? сюда передать
        GameBlock block = new GameBlock(TileRepository.GROUND);
        block.setWalkable(true);
        return block;
    }

    public GameBlock createGrass() {
        //TODO енам с типами блоков? сюда передать
        GameBlock block = new GameBlock(TileRepository.GRASS);
        block.setWalkable(true);
        return block;
    }
}
