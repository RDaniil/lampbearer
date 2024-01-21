package com.vdn.lampbearer.game.world.block;

import com.vdn.lampbearer.constants.BlockLightingState;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Actor;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.views.BlockTypes;
import com.vdn.lampbearer.views.TileRepository;
import kotlin.Pair;
import lombok.Getter;
import lombok.Setter;
import org.hexworks.zircon.api.color.TileColor;
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
        lightingState = BlockLightingState.UNSEEN;
        setTop(TileRepository.getTile(BlockTypes.UNSEEN));
    }


    private BlockLightingState lightingState;
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
        Tile newContent = getDisplayedTile();
        setContent(newContent);
        updateLighting();
    }


    public Tile getDisplayedTile() {
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
        return newContent;
    }


    public void updateLighting() {
        var newContent = getContent();
        if (lightingState == BlockLightingState.IN_LIGHT) {
            setTop(TileRepository.getTile(BlockTypes.EMPTY));
        } else if (lightingState == BlockLightingState.SEEN) {
            //TODO: Понять как не отображать сущности вне радиуса игрока

            newContent = getDisplayedTile().withForegroundColor(
                    newContent.getForegroundColor()
                            .darkenByPercent(0.4)
                            .desaturate(0.3));

        }
        setContent(newContent);
    }


    public void updateTileColor(TileColor tileColor) {
        setContent(getDisplayedTile().withForegroundColor(tileColor));
    }


    public boolean hasEntities() {
        return !entities.isEmpty();
    }
}
