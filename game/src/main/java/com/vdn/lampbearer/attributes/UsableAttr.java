package com.vdn.lampbearer.attributes;

import lombok.Getter;

@Getter
public class UsableAttr implements Attribute {
    private final int maxUses;
    private int usesLeft;


    public UsableAttr(int maxUses) {
        this.maxUses = maxUses;
        usesLeft = maxUses;
    }


    public UsableAttr(int maxUses, int usesLeft) {
        this.maxUses = maxUses;
        this.usesLeft = usesLeft;
    }


    /**
     * Уменьшает счетчик использования один раз
     *
     * @return Количество использованных использований ( 1, если использования еще есть. 0 если
     * использования израсходованы)
     */
    public int useOnce() {
        return use(1);
    }


    /**
     * Пытается использовать предмет {@code usesToUse} раз
     * @param usesToUse количество использований, которые нужно потратить
     * @return <b>количество фактических использований</b>, если {@code usesToUse} > оставшихся
     * использований
     *<br>
     *<br>
     * <b>{@code usesToUse}</b>, если {@code usesToUse} < оставшихся использований
     */
    private int use(int usesToUse) {
        if (usesToUse > usesLeft) {
            var usedUses = usesLeft;
            usesLeft = 0;
            return usedUses;
        } else {
            usesLeft -= usesToUse;
            return usesToUse;
        }
    }


    /**
     * Добавляет использования (но не больше разрешенных использований)
     *
     * @param usableAttr количество добавляемых использований
     * @return количество добавленных использований
     */
    public int addUses(UsableAttr usableAttr) {
        var usesToFill = maxUses - usesLeft;
        var availableUses = usableAttr.use(usesToFill);
        usesLeft += availableUses;
        return availableUses;
    }


    public int getPercentageLeft() {
        return (int) (((float) usesLeft / (float) maxUses) * 100);
    }


    public String getStringPercentageLeft() {
        int percentageLeft = getPercentageLeft();
        String getStringPercentageLeft;
        if (percentageLeft > 85) {
            getStringPercentageLeft = "Seems full";
        } else if (percentageLeft > 60) {
            getStringPercentageLeft = "More than half left";
        } else if (percentageLeft > 40) {
            getStringPercentageLeft = "About half left";
        } else if (percentageLeft > 25) {
            getStringPercentageLeft = "Less than half left";
        } else if (percentageLeft > 10) {
            getStringPercentageLeft = "Almost empty";
        } else if (percentageLeft > 1) {
            getStringPercentageLeft = "On it's last breath";
        } else {
            getStringPercentageLeft = "Completely empty";
        }
        return getStringPercentageLeft;
    }
}
