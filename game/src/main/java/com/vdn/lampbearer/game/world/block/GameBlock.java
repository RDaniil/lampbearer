package com.vdn.lampbearer.game.world.block;

import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Actor;
import com.vdn.lampbearer.entites.Player;
import kotlin.Pair;
import lombok.Getter;
import lombok.Setter;
import org.hexworks.zircon.api.data.BlockTileType;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.data.base.BaseBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kotlinx.collections.immutable.ExtensionsKt.persistentMapOf;

/**
 * Игровая ячейка\блок
 * Имеет символ (Tile)
 * Обладает атрибутами (прозрачность, проходимость)
 * Может содержать сущности (игрок стоит на земле)
 */
@Getter
@Setter
public class GameBlock extends BaseBlock<Tile> {
    public GameBlock(Tile tile) {
        super(tile, persistentMapOf(new Pair<>(BlockTileType.CONTENT, tile)));
    }


    private boolean isWalkable;
    private boolean isTransparent;

    private final ArrayList<AbstractEntity> entities = new ArrayList<>(3);


    public void addEntity(AbstractEntity entity) {
        entities.add(entity);
        updateContent();
    }


    public void removeEntity(AbstractEntity entity) {
        entities.remove(entity);
        updateContent();
    }


    public void updateContent() {

        List<AbstractEntity> actors = entities.stream()
                .filter(entity -> entity instanceof Actor)
                .collect(Collectors.toList());
        Tile newContent = getEmptyTile();

        if (actors.isEmpty()) {
            List<Tile> allEntitiesTiles = entities.stream()
                    .map(AbstractEntity::getTile)
                    .collect(Collectors.toList());
            if (!allEntitiesTiles.isEmpty()) {
                newContent = allEntitiesTiles.get(0);
            }
        } else {
            Optional<AbstractEntity> player = actors.stream()
                    .filter(entity -> entity instanceof Player)
                    .findFirst();
            if (player.isPresent()) {
                newContent = player.get().getTile();
            } else {
                newContent = actors.get(0).getTile();
            }
        }

        setContent(newContent);
    }
}
