package com.vdn.lampbearer.entites.behavior.ai.olfaction;

import com.vdn.lampbearer.attributes.creature.SmellAttr;
import com.vdn.lampbearer.entites.NonPlayerCharacter;
import com.vdn.lampbearer.entites.behavior.ai.Ai;
import com.vdn.lampbearer.game.GameContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.shape.LineFactory;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Линейный алгоритм
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SmellAi implements Ai {

    private static SmellAi INSTANCE;


    public static SmellAi getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new SmellAi());
    }


    public boolean isPlayerScentAround(NonPlayerCharacter npc, GameContext context) {
        Optional<SmellAttr> smellAttr = npc.findAttribute(SmellAttr.class);
        if (smellAttr.isEmpty()) return false;

        Optional<ArrayList<Position3D>> path = createPath(
                npc.getPosition(),
                context.getPlayer().getPosition()
        );
        return path.isPresent() && isPathInCircle(path.get(), smellAttr.get().getValue());
    }


    private boolean isPathInCircle(ArrayList<Position3D> path, int radius) {
        return path.size() <= radius + 1;
    }


    private Optional<ArrayList<Position3D>> createPath(Position3D startPosition,
                                                       Position3D endPosition) {
        ArrayList<Position3D> path = LineFactory.INSTANCE
                .buildLine(startPosition.to2DPosition(), endPosition.to2DPosition())
                .getPositions().stream()
                .map(p -> p.toPosition3D(0))
                .collect(Collectors.toCollection(ArrayList::new));
        return Optional.of(path);
    }
}
