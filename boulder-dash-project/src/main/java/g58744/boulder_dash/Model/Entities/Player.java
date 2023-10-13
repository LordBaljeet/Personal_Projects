package g58744.boulder_dash.Model.Entities;

import g58744.boulder_dash.Model.Position;

public class Player extends MovableEntity {
    private boolean isDead;
    public Player(Position pos) {
        super(EntityType.CHARACTER, pos);
        isDead = false;
    }
    public boolean isDead() {
        return isDead;
    }
    public void setDead(boolean dead) {
        isDead = dead;
        if(!dead) {
           setRepresentation("D");
        }
    }

}