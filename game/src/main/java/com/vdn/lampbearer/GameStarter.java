package com.vdn.lampbearer;

import com.vdn.lampbearer.views.PlayView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameStarter {

    private final PlayView playView;

    public void start() {
        playView.dock();
    }
}
