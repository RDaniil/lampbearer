package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.Interaction;
import com.vdn.lampbearer.action.reactions.DoorClosingReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DoorCloseAction implements Interaction<DoorClosingReaction> {

    private static DoorCloseAction INSTANCE;


    public static DoorCloseAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new DoorCloseAction());
    }


    @Override
    public Class<DoorClosingReaction> getReactionClass() {
        return DoorClosingReaction.class;
    }
}
