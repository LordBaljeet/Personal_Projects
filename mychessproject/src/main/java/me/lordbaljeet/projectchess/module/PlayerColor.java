package me.lordbaljeet.projectchess.module;

public enum PlayerColor {
    WHITE,BLACK;

    /**
     * This methode returns the opposite color of a given one.
     * @return returns the opposite color.
     */
    public PlayerColor opposite(){
        return this.equals(WHITE) ? BLACK : WHITE;
    }

    @Override
    public String toString() {
        return this.equals(WHITE) ? "White" : "Black";
    }
}