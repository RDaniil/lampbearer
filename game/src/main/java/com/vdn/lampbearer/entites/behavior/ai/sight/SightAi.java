package com.vdn.lampbearer.entites.behavior.ai.sight;

import com.vdn.lampbearer.attributes.PerceptionAttr;
import com.vdn.lampbearer.entites.NonPlayerCharacter;
import com.vdn.lampbearer.entites.behavior.ai.Ai;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.World;
import org.hexworks.zircon.api.data.Position3D;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Линейный алгоритм
 *
 * @author Chizhov D. on 2024.03.07
 */
public abstract class SightAi implements Ai {

    /**
     * Checks if player is in sight
     *
     * @param npc     NPC
     * @param context GameContext
     * @return true if player is in sight
     */
    public boolean isPlayerInSight(NonPlayerCharacter npc, GameContext context) {
        Optional<PerceptionAttr> perceptionAttr = npc.findAttribute(PerceptionAttr.class);
        if (perceptionAttr.isEmpty()) return false;

        World world = context.getWorld();
        Optional<ArrayList<Position3D>> path = createPath(
                npc.getPosition(),
                context.getPlayer().getPosition(),
                world
        );
        return path.isPresent() && isPathInCircle(path.get(), perceptionAttr.get().getValue()) &&
                path.get().stream().allMatch(world::isBlockTransparent);
    }


    private boolean isPathInCircle(ArrayList<Position3D> path, int radius) {
        return path.size() <= radius + 1;
    }


    /**
     * Creates a path between two positions
     *
     * @param startPosition start of path
     * @param endPosition   end of path
     * @param world         world
     * @return path which includes start and end
     */
    protected abstract Optional<ArrayList<Position3D>> createPath(Position3D startPosition,
                                                                  Position3D endPosition,
                                                                  World world);
}
