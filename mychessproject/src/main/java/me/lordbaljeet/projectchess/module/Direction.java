package me.lordbaljeet.projectchess.module;

public enum Direction {
    N(0,-1),
    S(0,1),
    E(-1,0),
    W(1,0),
    NW(1,-1),
    NE(-1,-1),
    SW(1,1),
    SE(-1,1)
    ;

    private int deltaX;
    private int deltaY;
    Direction(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }
}
