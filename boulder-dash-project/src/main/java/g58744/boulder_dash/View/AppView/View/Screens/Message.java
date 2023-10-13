package g58744.boulder_dash.View.AppView.View.Screens;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class Message extends Screen {

    private final String message;
    private final boolean isError;

    public Message(String message, boolean isError) {
        super(400, 100);
        this.message = message;
        this.isError = isError;
        setScreen();
        getStyleClass().add("message");
    }

    @Override
    void setScreen() {
        Label msg = new Label(message);
        msg.setTextFill(Color.RED);
        if(isError) {
            msg.getStyleClass().add("errorText");
        }else {
            msg.getStyleClass().add("messageText");
        }
        getChildren().add(msg);
        setAlignment(Pos.CENTER);
    }
}
