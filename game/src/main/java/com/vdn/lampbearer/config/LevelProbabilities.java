package com.vdn.lampbearer.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Chizhov D. on 2024.04.16
 */
@Getter
@AllArgsConstructor
public class LevelProbabilities {

    public static final LevelProbabilities DEFAULT = new LevelProbabilities(40, 25);

    private final float trailDeviation;
    private final float lamppostInstallation;
}
