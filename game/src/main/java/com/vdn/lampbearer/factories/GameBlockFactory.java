package com.vdn.lampbearer.factories;

import com.vdn.lampbearer.constants.GameCharacterConstants;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.views.TileRepository;
import org.springframework.stereotype.Service;

/**
 * Фабрика игровых блоков
 */
@Service
public class GameBlockFactory {

    //TODO: Это могила, надо переделать
    public GameBlock getBlockByChar(char block) {
        switch (block) {
            case GameCharacterConstants.GRASS:
                return createGrass();
            case GameCharacterConstants.GROUND:
                return createGround();
            case GameCharacterConstants.ROCK:
                return createRock();
            case GameCharacterConstants.UR_CORNER:
                return createUrCorner();
            case GameCharacterConstants.BR_CORNER:
                return createBrCorner();
            case GameCharacterConstants.UL_CORNER:
                return createUlCorner();
            case GameCharacterConstants.BL_CORNER:
                return createBlCorner();
            case GameCharacterConstants.V_WALL_THICK:
                return createVWallThick();
            case GameCharacterConstants.H_WALL_THICK:
                return createHWallThick();
        }

        return createGround();
    }

    public GameBlock createGround() {
        //TODO енам с типами блоков? сюда передать
        GameBlock block = new GameBlock(TileRepository.GROUND);
        block.setWalkable(true);
        block.setTransparent(true);
        return block;
    }

    public GameBlock createGrass() {
        //TODO енам с типами блоков? сюда передать
        GameBlock block = new GameBlock(TileRepository.GRASS);
        block.setWalkable(true);
        block.setTransparent(true);
        return block;
    }


    public GameBlock createRock() {
        //TODO енам с типами блоков? сюда передать
        GameBlock block = new GameBlock(TileRepository.ROCK);
        block.setWalkable(false);
        block.setTransparent(true);
        return block;
    }


    public GameBlock createUrCorner() {
        GameBlock block = new GameBlock(TileRepository.UR_CORNER);
        block.setWalkable(false);
        block.setTransparent(false);
        return block;
    }


    public GameBlock createBrCorner() {
        GameBlock block = new GameBlock(TileRepository.BR_CORNER);
        block.setWalkable(false);
        block.setTransparent(false);
        return block;
    }


    public GameBlock createUlCorner() {
        GameBlock block = new GameBlock(TileRepository.UL_CORNER);
        block.setWalkable(false);
        block.setTransparent(false);
        return block;
    }


    public GameBlock createBlCorner() {
        GameBlock block = new GameBlock(TileRepository.BL_CORNER);
        block.setWalkable(false);
        block.setTransparent(false);
        return block;
    }


    public GameBlock createHWallThick() {
        GameBlock block = new GameBlock(TileRepository.H_WALL_THICK);
        block.setWalkable(false);
        block.setTransparent(false);
        return block;
    }


    public GameBlock createVWallThick() {
        GameBlock block = new GameBlock(TileRepository.V_WALL_THICK);
        block.setWalkable(false);
        block.setTransparent(false);
        return block;
    }
}
