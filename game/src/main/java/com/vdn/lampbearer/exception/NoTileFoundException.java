package com.vdn.lampbearer.exception;

import org.hexworks.zircon.api.data.Tile;

public class NoTileFoundException extends RuntimeException {
    public NoTileFoundException(Tile tile) {
        super(String.format("Нет тайла со следующими параметрами: " +
                        "\n символ: %s" +
                        "\n цвет %s, %s, %s " +
                        "\n фон %s, %s, %s ",
                tile.asCharacterTileOrNull().getCharacter(),
                tile.asCharacterTileOrNull().getForegroundColor().getRed(),
                tile.asCharacterTileOrNull().getForegroundColor().getGreen(),
                tile.asCharacterTileOrNull().getForegroundColor().getBlue(),
                tile.asCharacterTileOrNull().getBackgroundColor().getRed(),
                tile.asCharacterTileOrNull().getBackgroundColor().getGreen(),
                tile.asCharacterTileOrNull().getBackgroundColor().getBlue()));
    }
}
