package com.vdn.lampbearer.entites.behavior.ai.movement;

import com.vdn.lampbearer.action.actions.interaction.DoorOpenAction;
import com.vdn.lampbearer.entites.NonPlayerCharacter;
import com.vdn.lampbearer.entites.behavior.ai.Ai;
import com.vdn.lampbearer.entites.objects.Door;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.World;
import com.vdn.lampbearer.game.world.block.GameBlock;
import org.hexworks.zircon.api.data.Position3D;

import java.util.ArrayList;
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

        if (context.getWorld().isBlockWalkable(nextPos)) {
            return context.getWorld().moveEntity(npc, nextPos);
        }

        return interact(npc, context, nextPos);
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

        World world = context.getWorld();
        Position3D playerPos = context.getPlayer().getPosition();
        if (!playerPos.equals(targetPos) && !world.isBlockWalkable(targetPos))
            return Optional.empty();

        return createPath(currentPos, targetPos, world);
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


    /**
     * @param npc        NPC
     * @param context    context
     * @param position3D позиция, на которой должно происходить взаимодействие
     * @return true если взаимодействие было выполнено, иначе false
     */
    private boolean interact(NonPlayerCharacter npc, GameContext context, Position3D position3D) {
        GameBlock block = context.getWorld().fetchBlockAtOrNull(position3D);
        if (block == null) return false;

        return block.getEntities().stream().anyMatch(e -> {
            if (e instanceof Door) {
                Optional<DoorOpenAction> action = e.findAction(DoorOpenAction.class);
                return action.map(a -> a.createReaction().execute(npc, e, context)).orElse(false);
            }

            return false;
        });
    }
}
