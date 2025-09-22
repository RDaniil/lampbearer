package com.vdn.lampbearer.services;

import com.vdn.lampbearer.dto.EnemyInfo;
import com.vdn.lampbearer.entites.AbstractEntity;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ScoreService {
    private final Map<EnemyInfo, Integer> enemyToKillCountMap = new HashMap<>();
    private Integer playerMovementCount = 0;
    private Integer healedAmount = 0;
    private Integer damageReceived = 0;
    private Integer damageDealt = 0;

    public void addKilledEnemy(AbstractEntity target) {
        var enemyInfo = new EnemyInfo(target.getName(), target.getTile());
        if(!enemyToKillCountMap.containsKey(enemyInfo)) {
            enemyToKillCountMap.put(enemyInfo, 0);
        }
        enemyToKillCountMap.put(enemyInfo, enemyToKillCountMap.get(enemyInfo) + 1);
    }

    public void addPlayerMove() {
        playerMovementCount++;
    }

    public Map<EnemyInfo, Integer> getKilledEnemiesMap() {
        return enemyToKillCountMap;
    }

    public Integer getPlayerMoveCounter() {
        return playerMovementCount;
    }

    public void addHealed(int healingAmount) {
        healedAmount += healingAmount;
    }

    public void addDamageReceived(int damageAmount) {
        damageReceived += damageAmount;
    }

    public void addDamageDealt(int damageAmount) {
        damageDealt += damageAmount;
    }
}
