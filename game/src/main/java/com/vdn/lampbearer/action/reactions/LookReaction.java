package com.vdn.lampbearer.action.reactions;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LookReaction implements TargetedReaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        context.getLogArea().addInlineComponent(target.getName() + " ", target.getTile().getForegroundColor());
        context.getLogArea().addInlineText(target.getName().length() + 1, target.getDescription());
        context.getLogArea().commitInlineElements();
        return false;
    }
}
