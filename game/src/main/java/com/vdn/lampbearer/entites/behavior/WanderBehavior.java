package com.vdn.lampbearer.entites.behavior;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.behavior.ai.MoveToAi;
import com.vdn.lampbearer.game.GameContext;
import org.hexworks.zircon.api.data.Position3D;

public class WanderBehavior implements Behavior {

    private boolean isMoving = false;
    private Position3D positionToMoveTo;

    MoveToAi moveToAi = new MoveToAi();

    @Override
    public void Act(AbstractEntity entity, GameContext context) {
        selectRandomPositionInView(entity, context);
//        if(!isMoving){
//            selectRandomPositionInView(entity, context);
//        }

        isMoving = moveToAi.execute(entity, positionToMoveTo, context);
    }

    private void selectRandomPositionInView(AbstractEntity entity, GameContext context) {
        positionToMoveTo = entity.getPosition()
                .withRelativeX(1)
                .withRelativeY(0);
    }
}
