package com.vdn.lampbearer.services.config;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TileGameBlockConfig {

    private static final Gson GSON = new Gson();

    public List<ConfigBlock> configTileList;


    public static TileGameBlockConfig parse(String json) {
        return GSON.fromJson(json, TileGameBlockConfig.class);
    }
}
