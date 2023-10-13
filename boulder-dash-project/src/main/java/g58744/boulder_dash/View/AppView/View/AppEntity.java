package g58744.boulder_dash.View.AppView.View;

import g58744.boulder_dash.Model.Entities.EntityType;
import javafx.scene.layout.Pane;

public class AppEntity extends Pane {
    private final EntityType entityType;
    private final int style;
    public AppEntity(EntityType type, int style) {
        this.entityType = type;
        this.style = style;
        getStyleClass().add("square");
        setBackground();
    }

    public EntityType getComponentType() {
        return entityType;
    }

    public int getStyleIndex() {
        return style;
    }
    private void setBackground(){
        if(entityType.getSpritePos() >= 0){
            int x = -(64*entityType.getSpritePos());
            int y = -(64*style);
            setStyle("-fx-background-position:"+x+" "+y);
        }else{
            setStyle("-fx-background-image: url("+entityType.getImagePath()+");");
        }
    }
}