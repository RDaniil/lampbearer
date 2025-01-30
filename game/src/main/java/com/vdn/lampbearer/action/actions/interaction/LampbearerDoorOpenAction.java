package com.vdn.lampbearer.action.actions.interaction;

import com.vdn.lampbearer.action.actions.Interaction;
import com.vdn.lampbearer.action.reactions.interaction.LambearerDoorOpeningReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LampbearerDoorOpenAction implements  Interaction<LambearerDoorOpeningReaction> {

    private static LampbearerDoorOpenAction INSTANCE;


    public static LampbearerDoorOpenAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new LampbearerDoorOpenAction());
    }


    @Override
    public Class<LambearerDoorOpeningReaction> getReactionClass() {
        return LambearerDoorOpeningReaction.class;
    }
}
