package com.vdn.lampbearer.action.actions;

import com.vdn.lampbearer.action.Action;
import com.vdn.lampbearer.action.Reaction;
import lombok.NoArgsConstructor;

@NoArgsConstructor()
public abstract class AbstractPickUpItemAction<R extends Reaction> implements Action<R> {

    @Override
    public String getName() {
        return "Pick up";
    }
}
