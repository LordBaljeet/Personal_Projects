package me.lordbaljeet.projectchess.view;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import me.lordbaljeet.projectchess.module.Game;
import me.lordbaljeet.projectchess.module.Player;
import me.lordbaljeet.projectchess.module.PlayerColor;

import java.util.Timer;
import java.util.TimerTask;

public class UserTimeInterface extends SplitPane {
    private Timer white_timer, black_timer;
    private Player whitePlayer, blackPlayer;
    private Text white_timer_text, black_timer_text;

    private Game game;
    public UserTimeInterface(Game game) {
        this.whitePlayer = new Player(PlayerColor.WHITE);
        this.blackPlayer = new Player(PlayerColor.BLACK);
        this.white_timer = new Timer();
        this.black_timer = new Timer();
        this.setDividerPositions(0.5);
        this.setLayoutX(0);
        this.setLayoutY(801);
        this.setPrefHeight(88);
        this.setPrefWidth(800);
        this.getItems().add(firstHalf());
        this.getItems().add(secondHalf());
        this.game = game;
    }
    private AnchorPane firstHalf() {
        AnchorPane leftSide = new AnchorPane();
        leftSide.setPrefHeight(160);
        leftSide.setPrefWidth(100);

        Label player1 = new Label("White Player");
        player1.setLayoutX(50);
        player1.setLayoutY(21);
        player1.setFont(Font.font("Times New Roman", 30));

        this.white_timer_text = new Text();
        white_timer_text.setText(Integer.toString(50));
        white_timer_text.setFill(Color.valueOf("#a509e3"));
        white_timer_text.setLayoutX(275);
        white_timer_text.setLayoutY(47);
        white_timer_text.setFont(Font.font("Times New Roman", 30));

        String timeString = String.format("%03d",whitePlayer.getTimeLeft());
        white_timer_text.setText(timeString);

        leftSide.getChildren().add(player1);
        leftSide.getChildren().add(white_timer_text);
        return leftSide;
    }
    private AnchorPane secondHalf() {
        AnchorPane rightSide = new AnchorPane();
        rightSide.setPrefHeight(160);
        rightSide.setPrefWidth(100);

        Label player2 = new Label("Black Player");
        player2.setLayoutX(150);
        player2.setLayoutY(21);
        player2.setFont(Font.font("Times New Roman", 30));

        this.black_timer_text = new Text();
        black_timer_text.setText(Integer.toString(100));
        black_timer_text.setFill(Color.valueOf("#a509e3"));
        black_timer_text.setLayoutX(50);
        black_timer_text.setLayoutY(47);
        black_timer_text.setFont(Font.font("Times New Roman", 30));

        String timeString = String.format("%03d",blackPlayer.getTimeLeft());
        black_timer_text.setText(timeString);
        rightSide.getChildren().add(player2);
        rightSide.getChildren().add(black_timer_text);
        return rightSide;
    }
    private void pause(Timer timer) {
        timer.cancel();
    }
    private Timer resume(Player player, Text timer) {
        Timer playerTimer = new Timer();
        TimerTask task = new TimerTask() {
            int time = player.getTimeLeft();
            @Override
            public void run() {
                if(time <= 0) {
                    pause(playerTimer);
                }
                else --time;
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        game.setWinScreen();
//                    }
//                });
                String timeString = String.format("%03d",time);
                timer.setText(timeString);
                player.setTimeLeft(time);
            }
        };
        playerTimer.schedule(task, 0, 1000);
        return playerTimer;
    }
    public void pausePlayer(Player player) {
        if (player.getColor() == whitePlayer.getColor()) {
            pause(white_timer);
        } else {
            pause(black_timer);
        }
    }
    public void resumePlayer(Player player) {
        if (player.getColor() == whitePlayer.getColor()) {
            white_timer = resume(player, white_timer_text);
        } else {
            black_timer = resume(player, black_timer_text);
        }
    }
}