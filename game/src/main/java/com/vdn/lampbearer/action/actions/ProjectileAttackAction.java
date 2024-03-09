package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.Action;
import com.vdn.lampbearer.action.reactions.ProjectileAttackReaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectileAttackAction implements Action<ProjectileAttackReaction> {

    private static ProjectileAttackAction INSTANCE;


    public static ProjectileAttackAction getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new ProjectileAttackAction());
    }


    @Override
    public Class<ProjectileAttackReaction> getReactionClass() {
        return ProjectileAttackReaction.class;
    }
}
