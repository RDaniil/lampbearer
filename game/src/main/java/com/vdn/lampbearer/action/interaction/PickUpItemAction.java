package com.vdn.lampbearer.action.interaction;

import com.vdn.lampbearer.action.reactions.PickUpItemReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PickUpItemAction implements Interaction<PickUpItemReaction> {

    private static PickUpItemAction INSTANCE;


    public static PickUpItemAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new PickUpItemAction());
    }


    @Override
    public String getName() {
        return "Pick up";
    }


    @Override
    public Class<PickUpItemReaction> getReactionClass() {
        return PickUpItemReaction.class;
    }
}
