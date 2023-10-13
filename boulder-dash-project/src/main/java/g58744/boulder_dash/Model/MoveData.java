package g58744.boulder_dash.Model;

import g58744.boulder_dash.Model.Entities.Entity;
import g58744.boulder_dash.Model.Entities.MovableEntity;

/**
 * A record of a move.
 * @param start The start position of the move
 * @param end The destination
 * @param predator The piece that moved
 * @param prey The piece that got consumed by the move
 */
public record MoveData(Position start, Position end, MovableEntity predator, Entity prey) {

    @Override
    public String toString() {
        return "moveData{" +
                "start=" + start +
                ", end=" + end +
                ", predator=" + predator +
                ", prey=" + prey +
                '}';
    }
}