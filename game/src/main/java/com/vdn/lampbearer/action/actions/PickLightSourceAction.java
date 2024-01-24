package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.reactions.DropLightSourceReaction;

public class PickLightSourceAction implements Interaction<DropLightSourceReaction> {

    private static PickLightSourceAction INSTANCE;


    public static PickLightSourceAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new PickLightSourceAction());
    }


    @Override
    public String getName() {
        return "Drop(Lighted)";
    }


    @Override
    public Class<DropLightSourceReaction> getReactionClass() {
        return DropLightSourceReaction.class;
    }
}