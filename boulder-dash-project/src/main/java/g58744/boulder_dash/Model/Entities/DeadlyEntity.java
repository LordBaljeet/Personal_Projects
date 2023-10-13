package g58744.boulder_dash.Model.Entities;

import g58744.boulder_dash.Model.Position;

public class DeadlyEntity extends MovableEntity {
    private boolean isFalling,isRolling,isRollingLeft;

    public DeadlyEntity(EntityType type, Position pos) {
        super(type, pos);
        isFalling = false;
        isRolling = false;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public void setAsFalling(boolean falling) {
        isFalling = falling;
    }

    public boolean isRolling() {
        return isRolling;
    }

    public void setRolling(boolean rolling) {
        isRolling = rolling;
    }

    public boolean isRollingLeft() {
        return isRollingLeft;
    }

    public void setRollingLeft(boolean rollingLeft) {
        isRollingLeft = rollingLeft;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeadlyEntity component)) return false;
        if (!super.equals(o)) return false;

        if (isFalling() != component.isFalling()) return false;
        if (isRolling() != component.isRolling()) return false;
        return isRollingLeft() == component.isRollingLeft();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (isFalling() ? 1 : 0);
        result = 31 * result + (isRolling() ? 1 : 0);
        result = 31 * result + (isRollingLeft() ? 1 : 0);
        return result;
    }
}
