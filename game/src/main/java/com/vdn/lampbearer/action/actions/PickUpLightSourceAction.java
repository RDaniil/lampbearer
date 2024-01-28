package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.reactions.PickUpLightSourceReaction;

public class PickUpLightSourceAction extends AbstractPickUpItemAction<PickUpLightSourceReaction> {

    private static PickUpLightSourceAction INSTANCE;


    public static PickUpLightSourceAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new PickUpLightSourceAction());
    }


    @Override
    public String getName() {
        return "Drop(Lighted)";
    }


    @Override
    public Class<PickUpLightSourceReaction> getReactionClass() {
        return PickUpLightSourceReaction.class;
    }
}