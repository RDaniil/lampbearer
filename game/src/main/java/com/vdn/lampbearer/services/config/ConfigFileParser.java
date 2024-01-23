package com.vdn.lampbearer.services.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ConfigFileParser {
    static ObjectMapper objectMapper = new ObjectMapper();
    private static final String PATH = "game/src/main/resources/configBlockFile.json";


    public static TileGameBlockConfig parseConfigFile() throws IOException {
        File file = new File(PATH);
        return objectMapper.readValue(file, TileGameBlockConfig.class);
    }
}
