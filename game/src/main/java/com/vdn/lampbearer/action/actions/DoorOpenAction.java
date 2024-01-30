package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.Interaction;
import com.vdn.lampbearer.action.reactions.DoorOpeningReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DoorOpenAction implements Interaction<DoorOpeningReaction> {

    private static DoorOpenAction INSTANCE;


    public static DoorOpenAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new DoorOpenAction());
    }


    @Override
    public Class<DoorOpeningReaction> getReactionClass() {
        return DoorOpeningReaction.class;
    }
}
