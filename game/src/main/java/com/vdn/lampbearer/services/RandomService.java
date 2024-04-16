package com.vdn.lampbearer.services;

import java.util.concurrent.ThreadLocalRandom;

public class RandomService {

    /**
     * @param min min число
     * @param max max число
     * @return случайное число из отрезка [min, max]
     */
    public static int getRandom(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }


    /**
     * @return случайное число из отрезка [1, 100]
     */
    public static int getRandomPercentage() {
        return RandomService.getRandom(1, 100);
    }
}
