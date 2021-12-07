package pl.lodz.chinczyk.pawn.model;

import lombok.NonNull;

import java.util.Arrays;

import static pl.lodz.chinczyk.pawn.model.Color.BLUE;
import static pl.lodz.chinczyk.pawn.model.Color.GREEN;
import static pl.lodz.chinczyk.pawn.model.Color.RED;
import static pl.lodz.chinczyk.pawn.model.Color.YELLOW;
import static pl.lodz.chinczyk.pawn.model.LocationType.BASE;
import static pl.lodz.chinczyk.pawn.model.LocationType.FIELD;
import static pl.lodz.chinczyk.pawn.model.LocationType.HOME;

public enum Location {
    R_0(RED, 0), R_1(RED, 1), R_2(RED, 2), R_3(RED, 3), R_4(RED, 4), R_5(RED, 5), R_6(RED, 6), R_7(RED, 7), R_8(RED, 8), R_9(RED, 9),
    G_0(GREEN, 0), G_1(GREEN, 1), G_2(GREEN, 2), G_3(GREEN, 3), G_4(GREEN, 4), G_5(GREEN, 5), G_6(GREEN, 6), G_7(GREEN, 7), G_8(GREEN, 8), G_9(GREEN, 9),
    B_0(BLUE, 0), B_1(BLUE, 1), B_2(BLUE, 2), B_3(BLUE, 3), B_4(BLUE, 4), B_5(BLUE, 5), B_6(BLUE, 6), B_7(BLUE, 7), B_8(BLUE, 8), B_9(BLUE, 9),
    Y_0(YELLOW, 0), Y_1(YELLOW, 1), Y_2(YELLOW, 2), Y_3(YELLOW, 3), Y_4(YELLOW, 4), Y_5(YELLOW, 5), Y_6(YELLOW, 6), Y_7(YELLOW, 7), Y_8(YELLOW, 8), Y_9(YELLOW, 9),
    R_BASE(RED, -2), R_HOME(RED, -1), G_BASE(GREEN, -2), G_HOME(GREEN, -1), B_BASE(BLUE, -2), B_HOME(BLUE, -1), Y_BASE(YELLOW, -2), Y_HOME(YELLOW, -1);

    private final int placeNumber;
    private final Color color;

    Location(Color color, int placeNumber) {
        this.placeNumber = placeNumber;
        this.color = color;
    }

    public static Location getBase(Color color) {
        return switch (color) {
            case RED -> R_BASE;
            case GREEN -> G_BASE;
            case BLUE -> B_BASE;
            case YELLOW -> Y_BASE;
        };
    }

    public LocationType getType() {
        return switch (this.placeNumber) {
            case -2 -> BASE;
            case -1 -> HOME;
            default -> FIELD;
        };
    }

    public Location getNewLocation(@NonNull Color color, int distance) {
        if (getType() == HOME) {
            return this;
        } else if (getType() == BASE) {
            if (distance == 6) {
                return switch (color) {
                    case RED -> R_0;
                    case BLUE -> B_0;
                    case GREEN -> G_0;
                    case YELLOW -> Y_0;
                };
            } else {
                return this;
            }
        } else {
            int nextPlace = this.placeNumber + distance;
            int moduloNextPlace = (this.placeNumber + distance) % 10;
            Color color1;
            if (nextPlace > 9) {
                color1 = switch (this.color) {
                    case RED -> GREEN;
                    case GREEN -> BLUE;
                    case BLUE -> YELLOW;
                    case YELLOW -> RED;
                };
                if (color == color1) {
                    return switch (color) {
                        case RED -> R_HOME;
                        case GREEN -> G_HOME;
                        case BLUE -> B_HOME;
                        case YELLOW -> Y_HOME;
                    };
                }
            } else {
                color1 = this.color;
            }
            return Arrays.stream(Location.values())
                    .filter(location -> location.placeNumber == moduloNextPlace && location.color == color1)
                    .findFirst().get();
        }
    }
}
