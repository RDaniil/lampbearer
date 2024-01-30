package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.reactions.InspectItemReaction;

public class InspectItemAction extends AbstractInspectAction<InspectItemReaction> {

    private static InspectItemAction INSTANCE;


    @Override
    public String getName() {
        return "Inspect";
    }


    public static InspectItemAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new InspectItemAction());
    }


    @Override
    public Class<InspectItemReaction> getReactionClass() {
        return InspectItemReaction.class;
    }
}
