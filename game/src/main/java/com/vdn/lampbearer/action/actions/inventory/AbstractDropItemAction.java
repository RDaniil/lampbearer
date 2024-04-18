package com.vdn.lampbearer.action.actions.inventory;

import com.vdn.lampbearer.action.actions.Action;
import com.vdn.lampbearer.action.reactions.Reaction;
import lombok.NoArgsConstructor;

@NoArgsConstructor()
public abstract class AbstractDropItemAction<R extends Reaction> implements Action<R> {
    @Override
    public String getName() {
        return "Drop";
    }
}
