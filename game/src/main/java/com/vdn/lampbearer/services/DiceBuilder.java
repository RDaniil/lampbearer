package com.vdn.lampbearer.services;

public class DiceBuilder {
    private int resultOfRoll = 0;

    public DiceBuilder dice(int numberOfRoll, int numberOfEdge) {
        if (numberOfRoll <= 0 || numberOfEdge <= 0) {
            throw new IllegalArgumentException("Args should be greater than zero");
        }

        for (int i = 0; i < numberOfRoll; i++) {
            this.resultOfRoll += RandomService.getRandom(1, numberOfEdge);
        }
        return this;
    }

    public DiceBuilder plus(int number) {
        this.resultOfRoll += number;
        return this;
    }

    public int roll() {
        return resultOfRoll;
    }
}
