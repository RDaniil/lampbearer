package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.Action;
import com.vdn.lampbearer.action.reactions.LoadFirearmReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoadFirearmAction implements Action<LoadFirearmReaction> {

    private static LoadFirearmAction INSTANCE;


    @Override
    public Class<LoadFirearmReaction> getReactionClass() {
        return LoadFirearmReaction.class;
    }


    @Override
    public String getName() {
        return "Load all";
    }


    public static LoadFirearmAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new LoadFirearmAction());
    }
}
