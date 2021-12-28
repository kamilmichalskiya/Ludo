package pl.lodz.chinczyk.pawn.model;

import lombok.NonNull;

public enum Color {
    RED, GREEN, BLUE, YELLOW, NO_COLOR;

    public static Color getNextColor(@NonNull Color color) {
        switch (color) {
            case RED:
                return GREEN;
            case GREEN:
                return BLUE;
            case BLUE:
                return YELLOW;
            case YELLOW:
                return RED;
            case NO_COLOR:
                return NO_COLOR;
            default:
                throw new IllegalArgumentException();
        }
    }
}
