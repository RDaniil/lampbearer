package com.vdn.lampbearer.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vdn.lampbearer.game.world.block.GameBlock;
import lombok.AllArgsConstructor;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author Chizhov D. on 2024.03.14
 */
@AllArgsConstructor
public class LevelsConfig {

    private static final Gson GSON = new Gson();

    private final Map<String, LevelConfig> levelToConfigMap;


    public Map<Position3D, GameBlock> getMap(Integer levelIndex, Size3D worldSize) {
        LevelConfig levelConfig = levelToConfigMap.get(levelIndex.toString());
        if (levelConfig == null) throw new RuntimeException("Level hasn't been configured!");

        return levelConfig.getMap(worldSize);
    }


    public static LevelsConfig parse(String json) {
        Type empMapType = new TypeToken<Map<String, LevelConfig>>() {
        }.getType();
        return new LevelsConfig(GSON.fromJson(json, empMapType));
    }
}
