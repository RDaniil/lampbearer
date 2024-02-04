package com.vdn.lampbearer.entites.behavior.player;

import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.entites.behavior.Behavior;
import org.hexworks.zircon.api.uievent.UIEvent;

public abstract class PlayerBehavior extends Behavior<Player> {
    public abstract boolean isUiEventApplicable(UIEvent event);
}
