package com.vdn.lampbearer.services.config;

import com.vdn.lampbearer.views.BlockTypes;
import lombok.Getter;
import lombok.Setter;

/**
 * Промежуточный класс для чтение Tile с JSON файла
 */
@Getter
@Setter
public class ConfigBlock {

    private BlockTypes blockType;

    private String background;

    private String foreground;

    private char tile;

    private boolean walkable;

    private boolean transparent;
}
