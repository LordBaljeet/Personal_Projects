package g58744.boulder_dash.View.AppView.View.Screens;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class EscapeScreen extends Screen {
    private Button home;

    private Button exit;

    public EscapeScreen(double width, double height) {
        super(width, height);
        setScreen();
        setVisible(false);
    }

    void setScreen(){
        VBox buttons = new VBox();
        home = new Button("Home");
        home.getStyleClass().add("gameBtn");
        exit = new Button("Exit");
        buttons.getChildren().addAll(home, exit);
        getChildren().addAll(buttons);
        buttons.setAlignment(Pos.CENTER_LEFT);
        buttons.setSpacing(60);
        this.setAlignment(Pos.CENTER);
    }

    public void setHandlers(EventHandler<ActionEvent> homeHandler, EventHandler<ActionEvent> exitHandler){
        home.addEventHandler(ActionEvent.ACTION, homeHandler);
        exit.addEventHandler(ActionEvent.ACTION, exitHandler);
    }
}
