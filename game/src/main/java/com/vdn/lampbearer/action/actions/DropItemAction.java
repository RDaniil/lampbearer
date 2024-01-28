package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.reactions.DropItemReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DropItemAction extends AbstractDropItemAction<DropItemReaction> {

    private static DropItemAction INSTANCE;


    public static DropItemAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new DropItemAction());
    }

    @Override
    public Class<DropItemReaction> getReactionClass() {
        return DropItemReaction.class;
    }
}
