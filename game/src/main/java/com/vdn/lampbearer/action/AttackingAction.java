package com.vdn.lampbearer.action;

import com.vdn.lampbearer.reactions.AttackingReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AttackingAction implements Action<AttackingReaction> {

    private static AttackingAction INSTANCE;


    public static AttackingAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new AttackingAction());
    }


    @Override
    public Class<AttackingReaction> getReactionClass() {
        return AttackingReaction.class;
    }
}
