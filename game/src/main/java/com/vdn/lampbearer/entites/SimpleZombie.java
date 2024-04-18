package com.vdn.lampbearer.entites;

import com.vdn.lampbearer.action.actions.combat.AttackAction;
import com.vdn.lampbearer.attributes.creature.*;
import com.vdn.lampbearer.attributes.occupation.StaticBlockOccupier;
import com.vdn.lampbearer.entites.behavior.ai.movement.AstarMovementAi;
import com.vdn.lampbearer.entites.behavior.ai.olfaction.SmellAi;
import com.vdn.lampbearer.entites.behavior.ai.sight.LinearSightAi;
import com.vdn.lampbearer.entites.behavior.npc.general.AttackingBehavior;
import com.vdn.lampbearer.entites.behavior.npc.general.ChasingBehavior;
import com.vdn.lampbearer.entites.behavior.npc.general.NonPlayerCharacterBehavior;
import com.vdn.lampbearer.entites.behavior.npc.general.WanderingBehavior;
import com.vdn.lampbearer.entites.interfaces.Schedulable;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.GameContext;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.data.Position3D;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class SimpleZombie extends NonPlayerCharacter implements Schedulable {

    private NonPlayerCharacterBehavior behavior = new AttackingBehavior();


    public SimpleZombie(Position3D position3D) {
        super(position3D);

        BlockType type = BlockType.SIMPLE_ZOMBIE;
        GameBlock block = GameBlockFactory.returnGameBlock(type);
        setName(block.getName());
        setDescription(block.getDescription());
        setTile(TileRepository.getTile(type));
        setAttributes(List.of(
                new HealthAttr(20),
                new StrengthAttr(0),
                new PerceptionAttr(10),
                new SmellAttr(10),
                new SpeedAttr(5),
                StaticBlockOccupier.getInstance()
        ));
        setActions(List.of(AttackAction.getInstance()));

        behaviors.add(new AttackingBehavior());
        behaviors.add(new ChasingBehavior(
                LinearSightAi.getInstance(),
                SmellAi.getInstance(),
                AstarMovementAi.getInstance(),
                true
        ));
        behaviors.add(new WanderingBehavior(AstarMovementAi.getInstance()));
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
