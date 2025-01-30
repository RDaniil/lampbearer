package com.vdn.lampbearer.action.actions.interaction;

import com.vdn.lampbearer.action.actions.Interaction;
import com.vdn.lampbearer.action.reactions.interaction.LighthouseDoorOpeningReaction;
import com.vdn.lampbearer.action.reactions.interaction.LighthouseSocketPutLampReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LighthouseSocketPutLampAction implements Interaction<LighthouseSocketPutLampReaction> {

    private static LighthouseSocketPutLampAction INSTANCE;


    public static LighthouseSocketPutLampAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new LighthouseSocketPutLampAction());
    }


    @Override
    public Class<LighthouseSocketPutLampReaction> getReactionClass() {
        return LighthouseSocketPutLampReaction.class;
    }
}
