package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.reactions.DropLightSourceReaction;

public class DropLightSourceAction implements Interaction<DropLightSourceReaction> {

    private static DropLightSourceAction INSTANCE;


    public static DropLightSourceAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new DropLightSourceAction());
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