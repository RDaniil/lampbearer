package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.Action;
import com.vdn.lampbearer.action.reactions.Reaction;
import lombok.NoArgsConstructor;

@NoArgsConstructor()
public abstract class AbstractPickUpItemAction<R extends Reaction> implements Action<R> {

    @Override
    public String getName() {
        return "Pick up";
    }
}
