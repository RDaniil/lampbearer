package com.vdn.lampbearer.game;

import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.game.world.World;
import com.vdn.lampbearer.services.ScoreService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hexworks.zircon.api.component.LogArea;
import org.hexworks.zircon.api.component.Panel;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.UIEvent;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameContext {
    //TODO: Этот объект до бесконечности разрастаться будет, надо чето придумать
    private World world;
    private Panel sidePanel;
    private UIEvent event;
    private Player player;
    private LogArea logArea;
    private Screen screen;
    private ScoreService scoreService;
}
