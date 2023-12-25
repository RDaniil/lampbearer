package com.vdn.lampbearer.action.interaction;

import com.vdn.lampbearer.reactions.DoorClosingReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DoorClosingAction implements Interaction<DoorClosingReaction> {

    private static DoorClosingAction INSTANCE;


    public static DoorClosingAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new DoorClosingAction());
    }


    @Override
    public Class<DoorClosingReaction> getReactionClass() {
        return DoorClosingReaction.class;
    }
}
