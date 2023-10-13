package g58744.boulder_dash.Model;

/**
 * Represents a direction(N,S,E,W).
 */
public enum Direction {
    N(0,-1),S(0,1), W(-1,0), E(1,0);

    final int deltaX;
    final int deltaY;

    Direction(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }


}
