package com.vdn.lampbearer.action;

import com.vdn.lampbearer.action.reactions.HealReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HealAction implements Action<HealReaction> {

    @Override
    public String getName() {
        return "Heal";
    }


    private static HealAction INSTANCE;


    public static HealAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new HealAction());
    }


    @Override
    public Class<HealReaction> getReactionClass() {
        return HealReaction.class;
    }
}
