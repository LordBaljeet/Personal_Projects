package g58744.boulder_dash.View.AppView.View;

import g58744.boulder_dash.View.AppView.View.Screens.HomePage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AppWindow extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        int width = (int) Screen.getPrimary().getBounds().getWidth();
        int height = (int) Screen.getPrimary().getBounds().getHeight();
        Scene scene = new Scene(new HomePage(height,width), width, height);
        scene.setFill(Color.BLACK);
        scene.getStylesheets().add("styling/stylesheet.css");
        scene.setFill(Color.WHITE);
        stage.setScene(scene);
        stage.setTitle("Boulder Dash");
        stage.setFullScreen(true);
        stage.setResizable(false);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        Image icon = new Image("images/logo.png");
        stage.getIcons().add(icon);
        stage.show();
    }

}