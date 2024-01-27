package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.reactions.PickUpItemReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PickUpItemAction extends AbstractPickUpItemAction<PickUpItemReaction> {

    private static PickUpItemAction INSTANCE;


    public static PickUpItemAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new PickUpItemAction());
    }


    @Override
    public Class<PickUpItemReaction> getReactionClass() {
        return PickUpItemReaction.class;
    }
}
