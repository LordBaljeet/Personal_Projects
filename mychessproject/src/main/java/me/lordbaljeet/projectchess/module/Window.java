package me.lordbaljeet.projectchess.module;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Window extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Game root = new Game();
        Board b = root.getBoard();
        root.getChildren().add(b);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(false);
        stage.setTitle("ProjectChess");
        Image icon = new Image("/Donkey.jpg");
        stage.getIcons().add(icon);
        stage.initStyle(StageStyle.DECORATED);
        root.start();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
