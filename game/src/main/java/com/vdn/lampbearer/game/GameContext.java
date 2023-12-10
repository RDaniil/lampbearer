package com.vdn.lampbearer.game;

import com.vdn.lampbearer.entites.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.UIEvent;

@Getter
@Setter
@AllArgsConstructor
public class GameContext {
    World world;
    Screen screen;
    UIEvent event;
    Player player;
}
