package com.vdn.lampbearer.services;

public class RandomService {
    public static int getRandom(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
