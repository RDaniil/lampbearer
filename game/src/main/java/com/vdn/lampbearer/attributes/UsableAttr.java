package com.vdn.lampbearer.attributes;

import lombok.Getter;

@Getter
public class UsableAttr implements Attribute {
    private final int maxUses;
    private int used;


    public UsableAttr(int maxUses) {
        this.maxUses = maxUses;
        used = maxUses;
    }


    /**
     * Уменьшает счетчик использования
     *
     * @return true, если использования еще есть. false если использования израсходованы
     */
    public boolean use() {
        used--;
        return used > 0;
    }
}
