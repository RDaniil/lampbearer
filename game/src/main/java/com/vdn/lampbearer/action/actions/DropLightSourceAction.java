package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.reactions.DropLightSourceReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DropLightSourceAction extends AbstractDropItemAction<DropLightSourceReaction> {

    private static DropLightSourceAction INSTANCE;


    public static DropLightSourceAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new DropLightSourceAction());
    }


    @Override
    public Class<DropLightSourceReaction> getReactionClass() {
        return DropLightSourceReaction.class;
    }
}