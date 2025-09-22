package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.reactions.revolver.LoadOneFromBoxFirearmReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoadOneFromBoxFirearmAction implements Action<LoadOneFromBoxFirearmReaction> {

    private static LoadOneFromBoxFirearmAction INSTANCE;


    @Override
    public Class<LoadOneFromBoxFirearmReaction> getReactionClass() {
        return LoadOneFromBoxFirearmReaction.class;
    }


    @Override
    public String getName() {
        return "Put one";
    }


    public static LoadOneFromBoxFirearmAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new LoadOneFromBoxFirearmAction());
    }
}
