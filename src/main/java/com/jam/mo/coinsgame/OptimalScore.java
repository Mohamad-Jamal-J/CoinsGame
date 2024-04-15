package com.jam.mo.coinsgame;


public class OptimalScore {
    int playerOneScore;
    int remainder;
    byte directionArrow = -1;

    public OptimalScore() {
    }

    public OptimalScore(int first, int second) {
        this.playerOneScore = first;
        this.remainder = second;
    }

    public void setDirectionArrow(byte directionArrow) {
        this.directionArrow = directionArrow;
    }

    public String alignScore(int value) {
        return value >= 1 && value <= 9 ? "0" + value : String.valueOf(value);
    }

    public String toString() {
        String d = ".";
        if (this.directionArrow == 1)
            d = "👉";
        else if (this.directionArrow == 0)
            d = "👆";

        return "(" + this.alignScore(this.playerOneScore) + "," + this.alignScore(this.remainder) + ")" + d;
    }
}
