package g58744.boulder_dash.View.AppView.View.Screens;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class WinScreen extends Screen {
    private Button abandon;
    private Button nextLvl;

    public WinScreen(double width, double height, boolean hasNext) {
        super(width, height);
        setScreen();
        nextLvl.setDisable(!hasNext);
        getStyleClass().add("message");
    }

    @Override
    void setScreen() {
        Label msg = new Label("YOU WIN");
        msg.getStyleClass().add("screenMsg");
        HBox buttons = new HBox();
        abandon = new Button("Home");
        abandon.getStyleClass().add("messageBtn");
        nextLvl = new Button("Next level");
        nextLvl.getStyleClass().add("messageBtn");
        buttons.getChildren().addAll(nextLvl, abandon);
        getChildren().addAll(msg, buttons);
        buttons.setAlignment(Pos.CENTER);
        setAlignment(Pos.CENTER);
        getStyleClass().add("gameScreen");
        setSpacing(60);
    }

    public void setHandlers(EventHandler<ActionEvent> nextLvlHandler, EventHandler<ActionEvent> abandonHandler){
        abandon.addEventHandler(ActionEvent.ACTION, abandonHandler);
        nextLvl.addEventHandler(ActionEvent.ACTION, nextLvlHandler);
    }
}