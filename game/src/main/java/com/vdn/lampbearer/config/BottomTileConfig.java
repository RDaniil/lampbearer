package com.vdn.lampbearer.config;

import com.vdn.lampbearer.views.BlockType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author Chizhov D. on 2024.03.14
 */
@Getter
@AllArgsConstructor
public class BottomTileConfig {

    private final List<BlockType> blockTypes;
    private final Integer minSize;
    private final Integer maxSize;
    private final Integer minNumber;
    private final Integer maxNumber;
}
