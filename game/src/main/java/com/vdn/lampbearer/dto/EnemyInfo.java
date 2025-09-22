package com.vdn.lampbearer.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hexworks.zircon.api.data.Tile;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class EnemyInfo {
    private final String name;
    private final Tile tile;
}
