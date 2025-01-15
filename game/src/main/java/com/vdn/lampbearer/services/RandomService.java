package com.vdn.lampbearer.services;


import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Random;

public class RandomService {

    private static final String PATH_TO_SEED = "./seed.txt";
    private static Random RANDOM;
    private static long SEED;

    public static void initRandom() {
        try {
            String seed = new String(Files.readAllBytes(Path.of(PATH_TO_SEED)));
            SEED = Long.getLong(seed);
        } catch (Exception e) {
            SEED = LocalDateTime.now().getNano();
        }

        SEED = 1337L;

        RANDOM = new Random(SEED);
    }

    /**
     * @param min min число
     * @param max max число
     * @return случайное число из отрезка [min, max]
     */
    public static int getRandom(int min, int max) {
        return min + RANDOM.nextInt(max - min + 1);
    }


    /**
     * @return случайное число из отрезка [1, 100]
     */
    public static int getRandomPercentage() {
        return RandomService.getRandom(1, 100);
    }
}
