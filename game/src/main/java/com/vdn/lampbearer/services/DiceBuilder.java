package com.vdn.lampbearer.services;

import java.io.Serializable;

public class DiceBuilder implements Serializable {
    private int resultOfRoll = 0;
    private int numberOfRoll = 0;
    private int numberOfEdge = 0;
    private int plus = 0;

    public static DiceBuilder dice(int numberOfRoll, int numberOfEdge) {
        DiceBuilder diceBuilder = new DiceBuilder();
        diceBuilder.numberOfRoll = numberOfRoll;
        diceBuilder.numberOfEdge = numberOfEdge;
        return diceBuilder;
    }

    public DiceBuilder plus(int number) {
        this.plus = number;
        return this;
    }
        //TODO: выводить в консоль цвета предметов в соответствии с тайлом
    private void dice() {
        if (numberOfRoll <= 0 || numberOfEdge <= 0) {
            throw new IllegalArgumentException("Args should be greater than zero");
        }

        for (int i = 0; i < numberOfRoll; i++) {
            this.resultOfRoll += RandomService.getRandom(1, numberOfEdge);
        }
    }

    private void plus() {
        this.resultOfRoll += plus;
    }

    public int roll() {
        dice();
        plus();

        var result = resultOfRoll;
        resultOfRoll = 0;
        return result;
    }
}
