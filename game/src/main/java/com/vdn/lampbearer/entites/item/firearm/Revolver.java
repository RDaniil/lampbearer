package com.vdn.lampbearer.entites.item.firearm;

import com.vdn.lampbearer.action.actions.inventory.DropItemAction;
import com.vdn.lampbearer.action.actions.inventory.PickUpItemAction;
import com.vdn.lampbearer.action.actions.items.ShootFirearmAction;
import com.vdn.lampbearer.attributes.RoundContainerAttr;
import com.vdn.lampbearer.attributes.items.HealingItemAttribute;
import com.vdn.lampbearer.attributes.items.UsableAttr;
import com.vdn.lampbearer.entites.item.projectile.Round;
import com.vdn.lampbearer.entites.item.projectile.revolver.AbstractRevolverRound;
import com.vdn.lampbearer.entites.item.projectile.revolver.DefaultRevolverRound;
import com.vdn.lampbearer.entites.item.projectile.revolver.EmptyRound;
import com.vdn.lampbearer.entites.item.projectile.revolver.SignalRevolverRound;
import com.vdn.lampbearer.factories.GameBlockFactory;
import com.vdn.lampbearer.game.world.block.GameBlock;
import com.vdn.lampbearer.services.DiceBuilder;
import com.vdn.lampbearer.views.BlockType;
import com.vdn.lampbearer.views.TileRepository;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.data.Position3D;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class Revolver extends AbstractFirearm<AbstractRevolverRound> {
    private final static int CYLINDER_CHAMBERS_COUNT = 6;
    private final List<AbstractRevolverRound> cylinder =
            Arrays.asList(new AbstractRevolverRound[CYLINDER_CHAMBERS_COUNT]);
    private int currentChamber = 0;


    private Revolver(Position3D position) {
        super(position);
        setTile(TileRepository.getTile(BlockType.REVOLVER));
        GameBlock gameBlock = GameBlockFactory.returnGameBlock(BlockType.REVOLVER);
        setName(gameBlock.getName());
        setDescription(gameBlock.getDescription());
        setAttributes(List.of(
                        new HealingItemAttribute(new DiceBuilder().dice(2, 6).plus(1).roll()),
                        new UsableAttr(1)
                )
        );

        getActions().add(ShootFirearmAction.getInstance());
    }


    public static Revolver createForWorld(Position3D position) {
        Revolver revolver = new Revolver(position);
        revolver.getActions().add(PickUpItemAction.getInstance());
        revolver.cylinder.set(0, new SignalRevolverRound(position));
        revolver.cylinder.set(1, new DefaultRevolverRound(position));
        revolver.cylinder.set(2, new DefaultRevolverRound(position));
        revolver.cylinder.set(3, new DefaultRevolverRound(position));
        revolver.cylinder.set(4, new DefaultRevolverRound(position));
        revolver.cylinder.set(5, new DefaultRevolverRound(position));
        return revolver;
    }


    public static Revolver createForPlayer(Position3D position) {
        Revolver revolver = new Revolver(position);
        revolver.getActions().add(DropItemAction.getInstance());
        revolver.cylinder.set(0, new SignalRevolverRound(position));
        revolver.cylinder.set(1, new SignalRevolverRound(position));
        revolver.cylinder.set(2, new SignalRevolverRound(position));
        revolver.cylinder.set(3, new SignalRevolverRound(position));
        revolver.cylinder.set(4, new SignalRevolverRound(position));
        revolver.cylinder.set(5, new SignalRevolverRound(position));
        return revolver;
    }


    public void rotateCylinderClockwise() {
        setCurrentChamber((currentChamber + 1) % CYLINDER_CHAMBERS_COUNT);
    }


    public void rotateCylinderCounterClockwise() {
        if (currentChamber - 1 < 0) {
            setCurrentChamber(CYLINDER_CHAMBERS_COUNT - 1);
        } else {
            setCurrentChamber(currentChamber - 1);
        }
    }


    public void setCurrentChamber(int currentChamber) {
        if (currentChamber >= cylinder.size()) {
            throw new IllegalArgumentException("В револьвере " + cylinder.size() + " камор. " +
                    "Нельзя указать " + currentChamber);
        }
        this.currentChamber = currentChamber;
    }


    @Override
    public int loadAllRounds(RoundContainerAttr ammoBox) {
        int loadedRounds = 0;

        for (int i = 0; i < cylinder.size(); i++) {
            if (cylinder.get(i) instanceof EmptyRound) {
                var round = ammoBox.popItem();
                if (round.isEmpty()) {
                    break;
                }
                loadedRounds++;
                cylinder.set(i, (AbstractRevolverRound) round.get());
            }
        }
        return loadedRounds;
    }


    @Override
    public int loadOneRound(Round round) {
        int loadedRounds = 0;

        for (int i = 0; i < cylinder.size(); i++) {
            if (cylinder.get(i) instanceof EmptyRound) {
                loadedRounds++;
                cylinder.set(i, (AbstractRevolverRound) round);
                break;
            }
        }
        return loadedRounds;
    }


    @Override
    @Nullable
    public AbstractRevolverRound popProjectile() {
        var projectile = cylinder.get(currentChamber);
        cylinder.set(currentChamber, new EmptyRound(Position3D.defaultPosition()));
        rotateCylinderClockwise();
        log.info("REVOLVER: chamber " + currentChamber);
        cylinder.forEach(ch -> log.info(ch instanceof EmptyRound ? "o" : "*"));

        return projectile instanceof EmptyRound ? null : projectile;
    }


    @Override
    public int countEmptyRounds() {
        return (int) cylinder.stream()
                .filter(ch -> ch instanceof EmptyRound)
                .count();
    }


    @Override
    public Class<AbstractRevolverRound> getAllowedProjectileType() {
        return AbstractRevolverRound.class;
    }
}
