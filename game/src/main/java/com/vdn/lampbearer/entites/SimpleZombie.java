package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.action.AttackAction;
import com.vdn.lampbearer.attributes.HealthAttr;
import com.vdn.lampbearer.attributes.PerceptionAttr;
import com.vdn.lampbearer.attributes.SpeedAttr;
import com.vdn.lampbearer.attributes.StrengthAttr;
import com.vdn.lampbearer.attributes.occupation.StaticBlockOccupier;
import com.vdn.lampbearer.entites.behavior.npc.SimpleZombieBehavior;
import com.vdn.lampbearer.entites.behavior.npc.general.NonPlayerCharacterBehavior;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.views.BlockTypes;
import com.vdn.lampbearer.views.TileRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SimpleZombie extends NonPlayerCharacter implements Schedulable {

    public SimpleZombie(SpeedAttr speedAttr) {
        setName("Zombie");
        setTile(TileRepository.getTile(BlockTypes.SIMPLE_ZOMBIE));
        setAttributes(List.of(
                new HealthAttr(20),
                new StrengthAttr(5),
                new PerceptionAttr(10),
                speedAttr,
                StaticBlockOccupier.getInstance()
        ));
        setActions(List.of(AttackAction.getInstance()));
        behaviors.add(new SimpleZombieBehavior());
    }


    @Override
    public boolean makeAction(GameContext context) {
        for (NonPlayerCharacterBehavior behavior : behaviors) {
            if (behavior.isApplicable(this, context))
                return behavior.act(this, context);
        }
        return false;
    }


    @Override
    public boolean isStuck(GameContext context) {
        List<GameBlock> blocks = getSurroundingPositions().stream()
                .map(context.getWorld()::fetchBlockAtOrNull).filter(Objects::nonNull)
                .collect(Collectors.toList());

        for (GameBlock block : blocks) {
            if (!block.isWalkable()) continue;
            if (block.getEntities().isEmpty()) return false;

            for (AbstractEntity entity : block.getEntities()) {
                if (entity.findAttribute(StaticBlockOccupier.class).isEmpty() ||
                        entity instanceof Player &&
                                entity.findAction(AttackAction.class).isPresent())
                    return false;
            }
        }

        return true;
    }


    @Override
    public int getTime() {
        return findAttribute(SpeedAttr.class).get().getValue();
    }
}
