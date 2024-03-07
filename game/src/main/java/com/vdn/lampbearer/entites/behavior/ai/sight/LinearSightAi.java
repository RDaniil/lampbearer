package com.vdn.lampbearer.entites.behavior.ai.sight;

import com.vdn.lampbearer.game.world.World;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.shape.LineFactory;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Bresenham's line algorithm
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LinearSightAi extends SightAi {

    private static LinearSightAi INSTANCE;


    public static LinearSightAi getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new LinearSightAi());
    }


    @Override
    protected Optional<ArrayList<Position3D>> createPath(Position3D startPosition,
                                                         Position3D endPosition,
                                                         World world) {
        ArrayList<Position3D> path = LineFactory.INSTANCE
                .buildLine(startPosition.to2DPosition(), endPosition.to2DPosition())
                .getPositions().stream()
                .map(p -> p.toPosition3D(0))
                .collect(Collectors.toCollection(ArrayList::new));
        return Optional.of(path);
    }
}
