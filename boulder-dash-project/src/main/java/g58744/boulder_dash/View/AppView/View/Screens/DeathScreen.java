package g58744.boulder_dash.View.AppView.View.Screens;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class DeathScreen extends Screen {

    private Button restart;
    private Button abandon;

    public DeathScreen(double width, double height) {
        super(width,height);
        setScreen();
    }

     void setScreen(){
        Label msg = new Label("YOU DIED");
        msg.getStyleClass().add("screenMsg");
        HBox buttons = new HBox();
        restart = new Button("restart");
        restart.getStyleClass().add("gameBtn");
        abandon = new Button("abandon");
        abandon.getStyleClass().add("gameBtn");
        buttons.getChildren().addAll(restart,abandon);
        getChildren().addAll(msg, buttons);
        buttons.setAlignment(Pos.CENTER);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("gameScreen");
        this.setSpacing(60);
    }

    public void setHandlers(EventHandler<ActionEvent> restartHandler, EventHandler<ActionEvent> abandonHandler){
        restart.addEventHandler(ActionEvent.ACTION, restartHandler);
        abandon.addEventHandler(ActionEvent.ACTION, abandonHandler);
    }
}
