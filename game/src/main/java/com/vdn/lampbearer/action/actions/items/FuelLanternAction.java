package com.vdn.lampbearer.action.actions.items;

import com.vdn.lampbearer.action.reactions.items.FuelLanternReaction;

public class FuelLanternAction extends AbstractFuelItemAction<FuelLanternReaction> {

    private static FuelLanternAction INSTANCE;


    public static FuelLanternAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new FuelLanternAction());
    }


    @Override
    public Class<FuelLanternReaction> getReactionClass() {
        return FuelLanternReaction.class;
    }


    @Override
    public String getName() {
        return "Fill oil";
    }
}
