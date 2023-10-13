package g58744.boulder_dash.Model.Entities;

import g58744.boulder_dash.Model.Board;
import g58744.boulder_dash.Model.Position;

public class MovableEntity extends Entity {
    private Position pos;
    private boolean goingLeft = false;

    public MovableEntity(EntityType type, Position pos) {
        super(type);
        this.pos = pos;
    }

    /**
     * Moves the movable component to the destination in the board.
     * @param destination the destination
     * @param board the board
     */
    public void move(Position destination, Board board) {
        Position oldPos = pos;
        pos = destination;
        if(destination.x() != oldPos.x()){
            goingLeft = destination.x() < oldPos.x();
        }
        board.replaceEntity(destination,this);
        board.replaceEntity(oldPos,new Entity(EntityType.VOID));
    }

    public Position getPos() {
        return pos;
    }

    public boolean isGoingLeft() {
        return goingLeft;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovableEntity that)) return false;
        if (!super.equals(o)) return false;

        if (isGoingLeft() != that.isGoingLeft()) return false;
        return getPos() != null ? getPos().equals(that.getPos()) : that.getPos() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getPos() != null ? getPos().hashCode() : 0);
        result = 31 * result + (isGoingLeft() ? 1 : 0);
        return result;
    }
}
