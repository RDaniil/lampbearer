package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.Action;
import com.vdn.lampbearer.action.reactions.ThrowReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThrowAction implements Action<ThrowReaction> {

    private static ThrowAction INSTANCE;


    @Override
    public Class<ThrowReaction> getReactionClass() {
        return ThrowReaction.class;
    }


    @Override
    public String getName() {
        return "Throw";
    }


    public static ThrowAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new ThrowAction());
    }
}
