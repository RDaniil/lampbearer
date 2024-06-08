package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.reactions.LoadAllInFirearmReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoadAllInFirearmAction implements Action<LoadAllInFirearmReaction> {

    private static LoadAllInFirearmAction INSTANCE;


    @Override
    public Class<LoadAllInFirearmReaction> getReactionClass() {
        return LoadAllInFirearmReaction.class;
    }


    @Override
    public String getName() {
        return "Load all";
    }


    public static LoadAllInFirearmAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new LoadAllInFirearmAction());
    }
}
