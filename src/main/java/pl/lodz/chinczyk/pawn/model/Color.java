package pl.lodz.chinczyk.pawn.model;

import lombok.NonNull;

public enum Color {
    RED, GREEN, BLUE, YELLOW, NO_COLOR;

    public static Color getNextColor(@NonNull Color color) {
        return switch (color) {
            case RED -> GREEN;
            case GREEN -> BLUE;
            case BLUE -> YELLOW;
            case YELLOW -> RED;
            case NO_COLOR -> NO_COLOR;
        };
    }
}
