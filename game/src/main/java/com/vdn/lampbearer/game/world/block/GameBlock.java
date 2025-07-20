package com.vdn.lampbearer.game.world.block;

import com.vdn.lampbearer.constants.BlockLightingState;
import com.vdn.lampbearer.entites.AbstractEntity;
import com.vdn.lampbearer.entites.Actor;
import com.vdn.lampbearer.entites.Player;
import com.vdn.lampbearer.views.BlockType;
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
import java.util.Objects;
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

    private BlockLightingState lightingState = BlockLightingState.UNSEEN;
    private boolean isWalkable;
    private boolean isTransparent;
    private String name;
    private String description;

    private final ArrayList<AbstractEntity> entities = new ArrayList<>(3);


    public GameBlock(Tile tile) {
        super(tile, persistentMapOf(new Pair<>(BlockTileType.BOTTOM, tile)));
        setTop(TileRepository.getTile(BlockType.UNSEEN));
    }


    public boolean isTransparent() {
        return isTransparent && entities.stream().allMatch(AbstractEntity::isTransparent);
    }


    public void addEntity(AbstractEntity entity) {
        entities.add(entity);
        updateContent();
    }


    public void removeEntity(AbstractEntity entity) {
        if (!entities.remove(entity)) {
            throw new RuntimeException("Сущность " + entity.toString() +
                    " не существует на блоке " + this);
        }
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
        Tile newContent = getBottomTile();

        Player player = (Player) actors.stream()
                .filter(entity -> entity instanceof Player)
                .findFirst().orElse(null);
        //TODO здесь нпе когда стреляешь только что заряженым реваольвером
        if (actors.isEmpty() ||
                (player == null && !BlockLightingState.IN_LIGHT.equals(lightingState))) {
            List<Tile> notActorTiles = entities.stream()
                    .filter(entity -> !(entity instanceof Actor))
                    .map(AbstractEntity::getTile)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if (!notActorTiles.isEmpty()) {
                newContent = notActorTiles.get(0);
            }
        } else {
            newContent = player != null ? player.getTile() : actors.get(0).getTile();
        }

        return newContent;
    }


    public void updateLighting() {
        var newContent = getContent();
        if (lightingState == BlockLightingState.IN_LIGHT) {
            setTop(TileRepository.getTile(BlockType.EMPTY));
        } else if (lightingState == BlockLightingState.SEEN) {
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


    public boolean isUnseen() {
        return BlockLightingState.UNSEEN.equals(getLightingState());
    }


    public Tile getBottomTile() {
        return getTiles().get(BlockTileType.BOTTOM);
    }


    public void cloneEntities(ArrayList<AbstractEntity> entities) {
        entities.forEach(e -> this.entities.add(e.clone()));
    }


    public static GameBlock createEmpty() {
        return new GameBlock(TileRepository.getTile(BlockType.EMPTY));
    }
}
