package pl.lodz.chinczyk.pawn.model;

import lombok.NonNull;

import java.util.Arrays;

import static pl.lodz.chinczyk.pawn.model.Color.BLUE;
import static pl.lodz.chinczyk.pawn.model.Color.GREEN;
import static pl.lodz.chinczyk.pawn.model.Color.RED;
import static pl.lodz.chinczyk.pawn.model.Color.YELLOW;
import static pl.lodz.chinczyk.pawn.model.Color.getNextColor;
import static pl.lodz.chinczyk.pawn.model.LocationType.BASE;
import static pl.lodz.chinczyk.pawn.model.LocationType.FIELD;
import static pl.lodz.chinczyk.pawn.model.LocationType.HOME;

public enum Location {
    R_0(RED, 0), R_1(RED, 1), R_2(RED, 2), R_3(RED, 3), R_4(RED, 4), R_5(RED, 5), R_6(RED, 6), R_7(RED, 7), R_8(RED, 8), R_9(RED, 9), G_0(GREEN, 0),
    G_1(GREEN, 1), G_2(GREEN, 2), G_3(GREEN, 3), G_4(GREEN, 4), G_5(GREEN, 5), G_6(GREEN, 6), G_7(GREEN, 7), G_8(GREEN, 8), G_9(GREEN, 9),
    B_0(BLUE, 0), B_1(BLUE, 1), B_2(BLUE, 2), B_3(BLUE, 3), B_4(BLUE, 4), B_5(BLUE, 5), B_6(BLUE, 6), B_7(BLUE, 7), B_8(BLUE, 8), B_9(BLUE, 9),
    Y_0(YELLOW, 0), Y_1(YELLOW, 1), Y_2(YELLOW, 2), Y_3(YELLOW, 3), Y_4(YELLOW, 4), Y_5(YELLOW, 5), Y_6(YELLOW, 6), Y_7(YELLOW, 7), Y_8(YELLOW, 8),
    Y_9(YELLOW, 9), R_BASE(RED, -2), R_HOME(RED, -1), G_BASE(GREEN, -2), G_HOME(GREEN, -1), B_BASE(BLUE, -2), B_HOME(BLUE, -1), Y_BASE(YELLOW, -2),
    Y_HOME(YELLOW, -1);

    private final int placeNumber;
    private final Color color;

    Location(Color color, int placeNumber) {
        this.placeNumber = placeNumber;
        this.color = color;
    }

    public static Location getBase(@NonNull Color color) {
        return getLocationForColor(color, R_BASE, G_BASE, B_BASE, Y_BASE);
    }

    public static Location getHome(@NonNull Color color) {
        return getLocationForColor(color, R_HOME, G_HOME, B_HOME, Y_HOME);
    }

    public static Location getStartLocation(@NonNull Color color) {
        return getLocationForColor(color, R_0, G_0, B_0, Y_0);
    }

    private static Location getLocationForColor(Color color, Location rLocation, Location gLocation, Location bLocation, Location yLocation) {
        switch (color) {
            case RED:
                return rLocation;
            case GREEN:
                return gLocation;
            case BLUE:
                return bLocation;
            case YELLOW:
                return yLocation;
            default:
                throw new IllegalArgumentException();
        }
    }

    public LocationType getType() {
        switch (this.placeNumber) {
            case -2:
                return BASE;
            case -1:
                return HOME;
            default:
                return FIELD;
        }
    }

    public Location getLocationAfterMove(@NonNull Color colorOfPawn, int distance) {
        if (getType() == BASE && distance == 6) return getStartLocation(this.color);
        else if (getType() == HOME || getType() == BASE) return this;
        else if (distance == 0) return getBase(colorOfPawn);

        Color nextColor;
        int nextPlace = this.placeNumber + distance;
        if (nextPlace > 9) {
            nextColor = getNextColor(this.color);
            if (nextColor == colorOfPawn) return getHome(colorOfPawn);
        } else {
            nextColor = this.color;
        }

        return Arrays.stream(Location.values())
                .filter(location -> location.color == nextColor)
                .filter(location -> location.placeNumber == nextPlace % 10)
                .findFirst()
                .orElse(this);
    }
}
