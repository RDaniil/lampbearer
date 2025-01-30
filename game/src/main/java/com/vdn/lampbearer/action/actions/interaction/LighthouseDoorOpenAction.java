package com.vdn.lampbearer.action.actions.interaction;

import com.vdn.lampbearer.action.actions.Interaction;
import com.vdn.lampbearer.action.reactions.interaction.LighthouseDoorOpeningReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LighthouseDoorOpenAction implements Interaction<LighthouseDoorOpeningReaction> {

    private static LighthouseDoorOpenAction INSTANCE;


    public static LighthouseDoorOpenAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new LighthouseDoorOpenAction());
    }


    @Override
    public Class<LighthouseDoorOpeningReaction> getReactionClass() {
        return LighthouseDoorOpeningReaction.class;
    }
}
