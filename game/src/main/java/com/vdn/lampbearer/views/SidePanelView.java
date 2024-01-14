package com.vdn.lampbearer.views;

import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.views.fragments.sidepanel.SidePanelFragment;

public class SidePanelView {

    private final SidePanelFragment sidePanelFragment;
    private final GameContext context;


    public SidePanelView(GameContext gameContext) {
        context = gameContext;
        sidePanelFragment = new SidePanelFragment(gameContext);
        context.getSidePanel().addFragment(sidePanelFragment);
    }


    public void updateUI() {
        sidePanelFragment.updateContent(context);
    }
}
