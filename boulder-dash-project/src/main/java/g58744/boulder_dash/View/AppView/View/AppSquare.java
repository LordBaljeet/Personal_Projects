package g58744.boulder_dash.View.AppView.View;

import g58744.boulder_dash.Model.Entities.EntityType;
import javafx.scene.layout.StackPane;

public class AppSquare extends StackPane {
    private AppEntity component;
    public AppSquare(AppEntity appComp) {
        this.component = appComp;
        this.setPrefSize(64,64);
        this.setMinSize(64,64);
        AppEntity c = new AppEntity(EntityType.VOID, component.getStyleIndex());
        this.getChildren().addAll(c,component);
    }

    public AppEntity getComponent() {
        return component;
    }

    public void setComponent(AppEntity appC) {
        component = appC;
        this.getChildren().clear();
        AppEntity c = new AppEntity(EntityType.VOID, component.getStyleIndex());
        this.getChildren().addAll(c,appC);
    }

}