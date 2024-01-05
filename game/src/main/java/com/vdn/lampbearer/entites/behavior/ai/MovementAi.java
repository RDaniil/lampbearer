package com.vdn.lampbearer.entites.behavior.ai;

import com.vdn.lampbearer.entites.NonPlayerCharacter;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.World;
import org.hexworks.zircon.api.data.Position3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An artificial intelligence which is driving NPCs
 */
public abstract class MovementAi implements Ai {
    /**
     * Moves NPC to the target position
     *
     * @param npc       NPC
     * @param targetPos target position
     * @param context   GameContext
     * @return true if a move has been made
     */
    public boolean move(NonPlayerCharacter npc, Position3D targetPos, GameContext context) {
        //TODO: Почти наверняка возвращать булеан - не достаточно для других поведений
        //TODO: Входные параметры скорее всего надо генерализировать,
        // потмоу что мб нужно больше одного и разных типов

        Optional<ArrayList<Position3D>> path = findPath(npc, targetPos, context);
        if (path.isEmpty()) return false;

        ArrayList<Position3D> positions = path.get();
        Position3D nextPos = positions.get(1);
        return context.getWorld().isBlockWalkable(nextPos) &&
                context.getWorld().moveEntity(npc, nextPos);
    }


    /**
     * Looks for a path between NPC's position and target positions
     *
     * @param npc       NPC
     * @param targetPos target position
     * @param context   GameContext
     * @return Optional.empty() if targetPos equals to Player's position, or if targetPos is not
     * walkable, or if there is at least one object between NPC and target that isn't transparent.
     * Otherwise, returns Optional containing a list of positions (a path) between two positions
     * including NPC's position and targetPos
     */
    public Optional<ArrayList<Position3D>> findPath(NonPlayerCharacter npc,
                                                    Position3D targetPos,
                                                    GameContext context) {
        Position3D currentPos = npc.getPosition();
        if (npc.isStuck(context))
            throw new RuntimeException(String.format("%s is stuck!", npc.getName()));

        Position3D playerPos = context.getPlayer().getPosition();
        if (!playerPos.equals(targetPos) && !context.getWorld().isBlockWalkable(targetPos))
            return Optional.empty();

        ArrayList<Position3D> path = createPath(currentPos, targetPos);
        return isTargetVisible(path, context.getWorld()) ? Optional.of(path) : Optional.empty();
    }


    /**
     * Creates a path between two positions
     *
     * @param startPos start of path
     * @param endPos   end of path
     * @return path which includes start and end
     */
    protected abstract ArrayList<Position3D> createPath(Position3D startPos,
                                                        Position3D endPos);


    /**
     * Checks if all objects between start and finish of the path are transparent
     *
     * @param positions positions of created path
     * @param world     World
     * @return true if all objects between start and end of the path are transparent
     */
    private boolean isTargetVisible(List<Position3D> positions, World world) {
        for (int i = 1; i < positions.size() - 1; i++) {
            if (!world.isBlockTransparent(positions.get(i))) return false;
        }
        return true;
    }
}
