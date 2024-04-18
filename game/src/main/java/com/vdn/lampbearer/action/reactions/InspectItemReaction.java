package com.vdn.lampbearer.action.reactions;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.game.GameContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InspectItemReaction implements Reaction {

    @Override
    public boolean execute(AbstractEntity initiator, AbstractEntity inspectedItem,
                           GameContext context) {
        context.getLogArea()
                .addParagraph(String.format("%s: %s", inspectedItem.getName(),
                        inspectedItem.getDescription()), false, 0);
        //Своего рода костыль, чтобы не тратить ход на осмотр предмета, возвращаем false, чтобы
        // действие не засчитывалось как выполненное
        return false;
    }
}
