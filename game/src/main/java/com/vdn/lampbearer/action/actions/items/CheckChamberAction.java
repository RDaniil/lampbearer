package com.vdn.lampbearer.action.actions.items;

import com.vdn.lampbearer.action.actions.Action;
import com.vdn.lampbearer.action.reactions.revolver.CheckChamberReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckChamberAction implements Action<CheckChamberReaction> {

    private static CheckChamberAction INSTANCE;


    @Override
    public Class<CheckChamberReaction> getReactionClass() {
        return CheckChamberReaction.class;
    }


    @Override
    public String getName() {
        return "Check chamber";
    }


    public static CheckChamberAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new CheckChamberAction());
    }
}
