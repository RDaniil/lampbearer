package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.reactions.LoadOneInFirearmReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoadOneInFirearmAction implements Action<LoadOneInFirearmReaction> {

    private static LoadOneInFirearmAction INSTANCE;


    @Override
    public Class<LoadOneInFirearmReaction> getReactionClass() {
        return LoadOneInFirearmReaction.class;
    }


    @Override
    public String getName() {
        return "Load";
    }


    public static LoadOneInFirearmAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new LoadOneInFirearmAction());
    }
}
