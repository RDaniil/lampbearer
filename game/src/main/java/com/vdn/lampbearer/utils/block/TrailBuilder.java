package com.vdn.lampbearer.utils.block;

import com.vdn.lampbearer.config.LevelProbabilities;
import com.vdn.lampbearer.services.RandomService;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @author Chizhov D. on 2024.03.10
 */
public class TrailBuilder {

    public static ArrayList<Position3D> build(Position3D from,
                                              Position3D to,
                                              LevelProbabilities probabilities,
                                              Size3D worldSize) {

        int maxX = Math.max(from.getX(), to.getX());
        int maxY = Math.max(from.getY(), to.getY());
        int minX = Math.min(from.getX(), to.getX());
        int minY = Math.min(from.getY(), to.getY());

        Position3D position3D;
        ArrayList<Position3D> trail = new ArrayList<>();

        if (from.getX() <= to.getX()) {
            position3D = from.withY(minY);
            while (position3D.getX() <= maxX) {
                trail.add(position3D);

                position3D = position3D.withRelativeX(1);

                int random = RandomService.getRandomPercentage();
                if (random <= probabilities.getTrailDeviation()) {
                    position3D = position3D.withRelativeY(random % 2 == 0 ? 1 : -1);
                    position3D = correctY(position3D, worldSize);
                }
            }
        }

        if (from.getY() <= to.getY()) {
            position3D = from.withX(maxX);
            while (position3D.getY() <= maxY) {
                trail.add(position3D);

                position3D = position3D.withRelativeY(1);

                int random = RandomService.getRandomPercentage();
                if (random <= probabilities.getTrailDeviation()) {
                    position3D = position3D.withRelativeX(random % 2 == 0 ? 1 : -1);
                    position3D = correctX(position3D, worldSize);
                }
            }
        }

        if (from.getX() >= to.getX()) {
            position3D = from.withY(maxY);
            while (position3D.getX() >= minX) {
                trail.add(position3D);

                position3D = position3D.withRelativeX(-1);

                int random = RandomService.getRandomPercentage();
                if (random <= probabilities.getTrailDeviation()) {
                    position3D = position3D.withRelativeY(random % 2 == 0 ? 1 : -1);
                    position3D = correctY(position3D, worldSize);
                }
            }
        }

        if (from.getY() >= to.getY()) {
            position3D = from.withX(minX);
            while (position3D.getY() >= minY) {
                trail.add(position3D);

                position3D = position3D.withRelativeY(-1);

                int random = RandomService.getRandomPercentage();
                if (random <= probabilities.getTrailDeviation()) {
                    position3D = position3D.withRelativeX(random % 2 == 0 ? 1 : -1);
                    position3D = correctX(position3D, worldSize);
                }
            }
        }

        return trail;
    }


    @NotNull
    private static Position3D correctX(Position3D position3D, Size3D worldSize) {
        if (position3D.getX() < 0) {
            position3D = position3D.withX(0);
        } else if (position3D.getX() >= worldSize.getXLength()) {
            position3D = position3D.withX(worldSize.getXLength() - 1);
        }
        return position3D;
    }


    @NotNull
    private static Position3D correctY(Position3D position3D, Size3D worldSize) {
        if (position3D.getY() < 1) {
            position3D = position3D.withY(1);
        } else if (position3D.getY() >= worldSize.getYLength()) {
            position3D = position3D.withY(worldSize.getYLength() - 1);
        }
        return position3D;
    }
}
