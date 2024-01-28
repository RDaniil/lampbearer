package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.Action;
import com.vdn.lampbearer.action.reactions.PutOutLampReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PutOutLampAction implements Action<PutOutLampReaction> {

    @Override
    public String getName() {
        return "Put out";
    }


    private static PutOutLampAction INSTANCE;


    public static PutOutLampAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new PutOutLampAction());
    }


    @Override
    public Class<PutOutLampReaction> getReactionClass() {
        return PutOutLampReaction.class;
    }
}
