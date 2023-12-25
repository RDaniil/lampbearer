package com.vdn.lampbearer.game;

import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.game.world.World;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hexworks.zircon.api.component.LogArea;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.UIEvent;

@Getter
@Setter
@AllArgsConstructor
public class GameContext {
    private World world;
    private Screen screen;
    private UIEvent event;
    private Player player;
    private LogArea logArea;
}
