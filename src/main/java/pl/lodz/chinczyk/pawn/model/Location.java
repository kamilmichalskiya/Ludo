package pl.lodz.chinczyk.pawn.model;

import java.util.Arrays;

public enum Location {
    R_0(0), R_1(1), R_2(2), R_3(3), R_4(4), R_5(5), R_6(6), R_7(7), R_8(8), R_9(9), RB, RH,
    G_0(10), G_1(11), G_2(12), G_3(13), G_4(14), G_5(15), G_6(16), G_7(17), G_8(18), G_9(19), GB, GH,
    B_0(20), B_1(21), B_2(22), B_3(23), B_4(24), B_5(25), B_6(26), B_7(27), B_8(28), B_9(29), BB, BH,
    Y_0(30), Y_1(31), Y_2(32), Y_3(33), Y_4(34), Y_5(35), Y_6(36), Y_7(37), Y_8(38), Y_9(39), YB, YH;

    private final int place_number;

    Location() {
        this.place_number = -1;
    }

    Location(int i) {
        this.place_number = i;
    }

    public Location getNext() {
        if (this.place_number == -1)
            return this;
        else if (this == Y_9)
            return R_0;
        else
            return Arrays.stream(Location.values())
                    .filter(location -> location.place_number == this.place_number + 1)
                    .findFirst()
                    .orElse(this);
    }
}
