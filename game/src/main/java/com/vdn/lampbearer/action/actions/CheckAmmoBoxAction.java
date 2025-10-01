package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.reactions.revolver.CheckAmmoBoxReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckAmmoBoxAction implements Action<CheckAmmoBoxReaction> {

    private static CheckAmmoBoxAction INSTANCE;

    @Override
    public Class<CheckAmmoBoxReaction> getReactionClass() {
        return CheckAmmoBoxReaction.class;
    }


    @Override
    public String getName() {
        return "Check contents";
    }


    public static CheckAmmoBoxAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new CheckAmmoBoxAction());
    }
}
