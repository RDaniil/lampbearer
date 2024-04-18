package com.vdn.lampbearer.action.actions.items;

import com.vdn.lampbearer.action.actions.Action;
import com.vdn.lampbearer.action.reactions.items.LightLampReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LightLampAction implements Action<LightLampReaction> {

    @Override
    public String getName() {
        return "Light";
    }


    private static LightLampAction INSTANCE;


    public static LightLampAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new LightLampAction());
    }


    @Override
    public Class<LightLampReaction> getReactionClass() {
        return LightLampReaction.class;
    }
}
