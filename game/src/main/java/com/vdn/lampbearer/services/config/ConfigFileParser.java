package com.vdn.lampbearer.services.config;

import com.vdn.lampbearer.config.LevelsConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ConfigFileParser {
    private static final String CONFIG_BLOCK_FILE = "configBlockFile.json";
    private static final String LEVELS_CONFIG_FILE = "levels.json";


    public static TileGameBlockConfig parseConfigFile() throws IOException {
        String json = readFile(CONFIG_BLOCK_FILE);
        return TileGameBlockConfig.parse(json);
    }


    public static LevelsConfig parseLevelsConfig() throws IOException {
        String json = readFile(LEVELS_CONFIG_FILE);
        return LevelsConfig.parse(json);
    }


    private static String readFile(String filename) throws IOException {
        StringBuilder builder = new StringBuilder();

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream(filename)) {
            InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            for (String line; (line = reader.readLine()) != null; ) {
                builder.append(line);
            }
        }

        return builder.toString();
    }
}
