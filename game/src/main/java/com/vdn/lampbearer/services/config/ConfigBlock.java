package com.vdn.lampbearer.services.config;

import com.vdn.lampbearer.views.BlockType;
import lombok.Getter;
import lombok.Setter;

/**
 * Промежуточный класс для чтения Tile с JSON файла
 */
@Getter
@Setter
public class ConfigBlock {

    private BlockType blockType;

    private String background;

    private String foreground;

    private char tile;

    private boolean walkable;

    private boolean transparent;

    private String name;

    private String description;
}
