package com.vdn.lampbearer.game.world;

import com.vdn.lampbearer.game.GameBlock;
import kotlin.jvm.functions.Function1;
import lombok.Getter;
import lombok.Setter;
import org.hexworks.cobalt.core.api.behavior.DisposeState;
import org.hexworks.cobalt.databinding.api.value.ObservableValue;
import org.hexworks.zircon.api.builder.game.GameAreaBuilder;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.game.GameArea;
import org.hexworks.zircon.internal.game.InternalGameArea;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Делегат для реализиации миллиона методов GameArea
 * Содержит всю карту из блоков
 */
@Getter
@Setter
public class WorldDelegate implements GameArea<Tile, GameBlock> {

    GameArea<Tile, GameBlock> gameAreaDelegate;
    Map<Position3D, GameBlock> startingBlocks;

    public WorldDelegate(Size3D visibleSize, Size3D actualSize, Map<Position3D, GameBlock> startingBlocks) {
        this.gameAreaDelegate = GameAreaBuilder.<Tile, GameBlock>newBuilder()
                .withVisibleSize(visibleSize)
                .withActualSize(actualSize)
                .build();
        this.startingBlocks = startingBlocks;
        startingBlocks.forEach(this::setBlockAt);
    }

    @NotNull
    @Override
    public DisposeState getDisposeState() {
        return gameAreaDelegate.getDisposeState();
    }

    @Override
    public boolean getDisposed() {
        return gameAreaDelegate.getDisposed();
    }

    @Override
    public void dispose(@NotNull DisposeState disposeState) {
        gameAreaDelegate.dispose(disposeState);
    }

    @Override
    public void disposeWhen(@NotNull ObservableValue<Boolean> observableValue) {
        gameAreaDelegate.disposeWhen(observableValue);
    }

    @Override
    public void keepWhile(@NotNull ObservableValue<Boolean> observableValue) {
        gameAreaDelegate.keepWhile(observableValue);
    }

    @NotNull
    @Override
    public Size3D getActualSize() {
        return gameAreaDelegate.getActualSize();
    }

    @NotNull
    @Override
    public Position3D getVisibleOffset() {
        return gameAreaDelegate.getVisibleOffset();
    }

    @NotNull
    @Override
    public ObservableValue<Position3D> getVisibleOffsetValue() {
        return gameAreaDelegate.getVisibleOffsetValue();
    }

    @NotNull
    @Override
    public Size3D getVisibleSize() {
        return gameAreaDelegate.getVisibleSize();
    }

    @NotNull
    @Override
    public Position3D scrollBackwardBy(int i) {
        return gameAreaDelegate.scrollBackwardBy(i);
    }

    @NotNull
    @Override
    public Position3D scrollDownBy(int i) {
        return gameAreaDelegate.scrollDownBy(i);
    }

    @NotNull
    @Override
    public Position3D scrollForwardBy(int i) {
        return gameAreaDelegate.scrollForwardBy(i);
    }

    @NotNull
    @Override
    public Position3D scrollLeftBy(int i) {
        return gameAreaDelegate.scrollLeftBy(i);
    }

    @NotNull
    @Override
    public Position3D scrollOneBackward() {
        return gameAreaDelegate.scrollOneBackward();
    }

    @NotNull
    @Override
    public Position3D scrollOneDown() {
        return gameAreaDelegate.scrollOneDown();
    }

    @NotNull
    @Override
    public Position3D scrollOneForward() {
        return gameAreaDelegate.scrollOneForward();
    }

    @NotNull
    @Override
    public Position3D scrollOneLeft() {
        return gameAreaDelegate.scrollOneLeft();
    }

    @NotNull
    @Override
    public Position3D scrollOneRight() {
        return gameAreaDelegate.scrollOneRight();
    }

    @NotNull
    @Override
    public Position3D scrollOneUp() {
        return gameAreaDelegate.scrollOneUp();
    }

    @NotNull
    @Override
    public Position3D scrollRightBy(int i) {
        return gameAreaDelegate.scrollRightBy(i);
    }

    @Override
    public void scrollTo(@NotNull Position3D position3D) {
        gameAreaDelegate.scrollTo(position3D);
    }

    @NotNull
    @Override
    public Position3D scrollUpBy(int i) {
        return gameAreaDelegate.scrollUpBy(i);
    }

    @NotNull
    @Override
    public Map<Position3D, GameBlock> getBlocks() {
        return gameAreaDelegate.getBlocks();
    }

    @NotNull
    @Override
    public InternalGameArea<Tile, GameBlock> asInternalGameArea() {
        return gameAreaDelegate.asInternalGameArea();
    }

    @NotNull
    @Override
    public GameBlock fetchBlockAtOrElse(@NotNull Position3D position3D, @NotNull Function1<? super Position3D, ? extends GameBlock> function1) {
        return gameAreaDelegate.fetchBlockAtOrElse(position3D, function1);
    }

    @Nullable
    @Override
    public GameBlock fetchBlockAtOrNull(@NotNull Position3D position3D) {
        return gameAreaDelegate.fetchBlockAtOrNull(position3D);
    }

    @Override
    public boolean hasBlockAt(@NotNull Position3D position3D) {
        return gameAreaDelegate.hasBlockAt(position3D);
    }

    @Override
    public void setBlockAt(@NotNull Position3D position3D, @NotNull GameBlock gameBlock) {
        gameAreaDelegate.setBlockAt(position3D, gameBlock);
    }
}
