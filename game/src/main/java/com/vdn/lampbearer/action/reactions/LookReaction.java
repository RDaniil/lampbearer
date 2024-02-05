package com.vdn.lampbearer.action.reactions;

import com.vdn.lampbearer.action.TargetedReaction;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LookReaction extends TargetedReaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity target, GameContext context) {
        context.getLogArea().addParagraph(
                String.format("%s. %s", target.getName(), target.getDescription()),
                false,
                0);
        return true;
    }
}
