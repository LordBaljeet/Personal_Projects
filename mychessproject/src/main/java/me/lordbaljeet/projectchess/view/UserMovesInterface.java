package me.lordbaljeet.projectchess.view;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import me.lordbaljeet.projectchess.module.Game;

public class UserMovesInterface extends SplitPane {

    private Game game;
    private VBox white;
    private VBox black;

    public UserMovesInterface(Game game) {
        this.game = game;
        createMovesVBox();
        this.setDividerPositions(0.5);
        this.setLayoutX(800);
        this.setPrefHeight(800);
        this.setPrefWidth(200);
    }

    private void createMovesVBox() {

        Separator sep = new Separator();
        sep.setOrientation(Orientation.HORIZONTAL);

        this.white = new VBox();
        Label whiteLabel = new Label("White Player");
        white.getChildren().add(whiteLabel);
        white.getChildren().add(sep);
        white.setAlignment(Pos.TOP_CENTER);
        this.getItems().add(white);

        this.black = new VBox();
        Label blackLabel = new Label("Black Player");
        black.getChildren().add(blackLabel);
        black.getChildren().add(sep);
        black.setAlignment(Pos.TOP_CENTER);
        this.getItems().add(black);
    }
}
