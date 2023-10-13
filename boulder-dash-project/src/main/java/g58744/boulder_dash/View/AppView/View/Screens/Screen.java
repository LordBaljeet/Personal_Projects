package g58744.boulder_dash.View.AppView.View.Screens;

import javafx.scene.layout.VBox;

public abstract class Screen extends VBox {

    public Screen(double width, double height) {
        setPrefSize(width,height);
        setMaxSize(width,height);
        this.getStyleClass().add("gameScreen");
    }

    abstract void setScreen();
}