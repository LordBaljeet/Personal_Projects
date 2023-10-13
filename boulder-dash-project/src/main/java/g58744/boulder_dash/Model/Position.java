package g58744.boulder_dash.Model;

/**
 * Represents a position.
 * @param x The X-axis Position
 * @param y The Y-axis Position
 */
public record Position(int x, int y) {

    /**
     * Gets the new position after moving in the given direction
     * @param dir the direction
     * @return new position
     */
    public Position moveTo(Direction dir) {
        int x_ = x;
        int y_ = y;
        x_ += dir.deltaX;
        y_ += dir.deltaY;
        return new Position(x_, y_);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position position)) return false;
        return x == position.x && y == position.y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}