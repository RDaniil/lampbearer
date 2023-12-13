package com.vdn.lampbearer.services;

import java.util.concurrent.ThreadLocalRandom;

public class RandomService {
    public static int getRandom(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
