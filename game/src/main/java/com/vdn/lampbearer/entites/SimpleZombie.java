package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.action.actions.AttackAction;
import com.vdn.lampbearer.attributes.HealthAttr;
import com.vdn.lampbearer.attributes.PerceptionAttr;
import com.vdn.lampbearer.attributes.SpeedAttr;
import com.vdn.lampbearer.attributes.StrengthAttr;
import com.vdn.lampbearer.attributes.occupation.StaticBlockOccupier;
import com.vdn.lampbearer.entites.behavior.ai.LinearMovementAi;
import com.vdn.lampbearer.entites.behavior.npc.general.AttackingBehavior;
import com.vdn.lampbearer.entites.behavior.npc.general.ChasingBehavior;
import com.vdn.lampbearer.entites.behavior.npc.general.NonPlayerCharacterBehavior;
import com.vdn.lampbearer.entites.behavior.npc.general.WanderingBehavior;
import com.vdn.lampbearer.entites.interfaces.Schedulable;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.views.BlockTypes;
import com.vdn.lampbearer.views.TileRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class SimpleZombie extends NonPlayerCharacter implements Schedulable {

    private NonPlayerCharacterBehavior behavior = new AttackingBehavior();


    public SimpleZombie(SpeedAttr speedAttr) {
        setName(GameBlockFactory.returnGameBlock(BlockTypes.SIMPLE_ZOMBIE).getName());
        setDescription(GameBlockFactory.returnGameBlock(BlockTypes.SIMPLE_ZOMBIE)
                .getDescription());
        setTile(TileRepository.getTile(BlockTypes.SIMPLE_ZOMBIE));
        setAttributes(List.of(
                new HealthAttr(20),
                new StrengthAttr(0),
                new PerceptionAttr(10),
                speedAttr,
                StaticBlockOccupier.getInstance()
        ));
        setActions(List.of(AttackAction.getInstance()));

        behaviors.add(new AttackingBehavior());
        behaviors.add(new ChasingBehavior(LinearMovementAi.getInstance()));
        behaviors.add(new WanderingBehavior(LinearMovementAi.getInstance()));
    }


    @Override
    public boolean makeAction(GameContext context) {
        if (isStuck(context))
            throw new RuntimeException(String.format("%s is stuck!", getName()));

        NonPlayerCharacterBehavior nextBehavior = behavior.next(this, context);

//        log.error(String.format("%s -> %s", behavior.getClass().getSimpleName(), nextBehavior.getClass().getSimpleName()));

        return (behavior = nextBehavior).act(this, context);
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
