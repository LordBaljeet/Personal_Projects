package g58744.boulder_dash.Model.Entities;

public enum EntityType {
    CHARACTER(" P ", "MON.png",7),
    PLAYERL(" P ", "MON.png",8),
    PLAYERR(" P ", "GUS.png",7),
    DEAD(" X ", "dead.png",9),
    WALL(" W ", "ayo.png",0),
    DIRT(" = ", "CRAZY HAMBURGER.png",1),
    DIAMOND(" + ", "diamond.png",5),
    BORDER(" - ", "illusion.jpg",4),
    DOOR(" G ", "patrickdoor.png",6),
    ROCK(" R ", "Maoi.png",3),
    VOID("   ","",2);

    private final String representation;
    private final String imagePath;
    private final int spritePos;

    EntityType(String representation, String imagePath, int spritePos) {
        this.representation = representation;
        this.imagePath = imagePath;
        this.spritePos = spritePos;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getSpritePos() {
        return spritePos;
    }

    @Override
    public String toString() {
        return representation;
    }
}
