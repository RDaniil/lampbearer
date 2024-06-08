package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.reactions.ShootFirearmReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShootFirearmAction implements Action<ShootFirearmReaction> {

    private static ShootFirearmAction INSTANCE;


    @Override
    public Class<ShootFirearmReaction> getReactionClass() {
        return ShootFirearmReaction.class;
    }


    @Override
    public String getName() {
        return "Shoot";
    }


    public static ShootFirearmAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new ShootFirearmAction());
    }
}
