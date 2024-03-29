package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.Action;
import com.vdn.lampbearer.action.reactions.AttackReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AttackAction implements Action<AttackReaction> {

    private static AttackAction INSTANCE;


    public static AttackAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new AttackAction());
    }


    @Override
    public Class<AttackReaction> getReactionClass() {
        return AttackReaction.class;
    }
}
