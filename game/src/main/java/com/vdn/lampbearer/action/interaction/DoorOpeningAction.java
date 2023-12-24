package com.vdn.lampbearer.action.interaction;

import com.vdn.lampbearer.reactions.DoorOpeningReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DoorOpeningAction implements Interaction<DoorOpeningReaction> {

    private static DoorOpeningAction INSTANCE;


    public static DoorOpeningAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new DoorOpeningAction());
    }


    @Override
    public Class<DoorOpeningReaction> getReactionClass() {
        return DoorOpeningReaction.class;
    }
}
