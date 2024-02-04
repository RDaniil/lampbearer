package com.vdn.lampbearer.dto;

import com.vdn.lampbearer.action.Reaction;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.game.GameContext;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Хранит всю необходимую информацию для выполнения реакции.
 */
@Getter
@AllArgsConstructor
public class ItemUseReactionContextDto {
    AbstractEntity initiator;
    AbstractEntity target;
    GameContext context;
    Reaction reaction;
}
