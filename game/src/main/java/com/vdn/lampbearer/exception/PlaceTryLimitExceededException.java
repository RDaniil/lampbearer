package com.vdn.lampbearer.exception;

/**
 * @author Chizhov D. on 2024.03.11
 */
public class PlaceTryLimitExceededException extends Exception {

    public PlaceTryLimitExceededException() {
        super("Превышен лимит попыток разместить объект");
    }
}
