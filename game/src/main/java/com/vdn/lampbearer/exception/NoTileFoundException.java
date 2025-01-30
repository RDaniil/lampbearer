package com.vdn.lampbearer.exception;

import lombok.Getter;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.CharacterTile;
import org.hexworks.zircon.api.data.Tile;

import java.util.Objects;
import java.util.Set;

@Getter
public class NoTileFoundException extends RuntimeException {

    private CharacterTile tile;

    public NoTileFoundException(Tile tile) {
        super(String.format("CANNOT CREATE TILE: '%s'\t%s\t%s\n",
                Objects.requireNonNull(tile.asCharacterTileOrNull()).getCharacter(),
                colorToHex(tile.getForegroundColor()),
                colorToHex(tile.getBackgroundColor())));
        this.tile = tile.asCharacterTileOrNull();
    }


    public NoTileFoundException(Set<CharacterTile> notFoundTiles, String pathToFile) {
        super(getExceptionMessage(notFoundTiles, pathToFile));
    }


    private static String getExceptionMessage(Set<CharacterTile> notFoundTiles, String pathToFile) {
        return notFoundTiles.stream()
                .map(t -> String.format("PREFAB %s: '%s'\tF:%s\tB:%s\n",
                        pathToFile,
                        t.getCharacter(),
                        colorToHex(t.getForegroundColor()),
                        colorToHex(t.getBackgroundColor())))
                .reduce("\n", String::concat);
    }


    private static String colorToHex(TileColor color) {
        return "#" + Integer.toHexString(color.getRed())
                + Integer.toHexString(color.getGreen())
                + Integer.toHexString(color.getBlue());
    }
}
