package g58744.boulder_dash.View.AppView.View;

import g58744.boulder_dash.Model.Model;
import g58744.boulder_dash.Model.Observer;
import g58744.boulder_dash.View.AppView.View.Screens.Screen;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AppGame extends StackPane{
    private AppBoard board;
    private AppInfo infos;
    private Model game;
    private final int style;
    private final VBox messages;
    public AppGame(Model game, int style) {
        this.game = game;
        this.style = style;
        this.setAlignment(Pos.TOP_LEFT);
        messages = new VBox();
        messages.translateXProperty().bind(translateXProperty().negate());
        messages.translateYProperty().bind(translateYProperty().negate());
        messages.setAlignment(Pos.BOTTOM_RIGHT);
    }

    public void start(Model game){
        this.game = game;
        this.getChildren().clear();
        board = new AppBoard(game.getBoard(), style);
        infos = new AppInfo(game.getCurrentLvl());
        infos.setInfoSection();
        infos.translateXProperty().bind(translateXProperty().negate());
        infos.translateYProperty().bind(translateYProperty().negate());
//        board.setTranslateY(64);
        getChildren().addAll(board,infos,messages);
    }

    public void updateBoard(){
        board.update();
    }

    public void updateInfos(){
        infos.updateCollectedD(game.getCollectedDiamonds());
    }

    public void stopTimer(){
        infos.stopTimer();
    }

    public void resumeTimer(){infos.resumeTimer();}

    public void setScreen(Screen s){
        getChildren().add(s);
        s.translateXProperty().bind(translateXProperty().negate());
        s.translateYProperty().bind(translateYProperty().negate());
    }

    public void addMessage(Screen s){
        messages.getChildren().add(s);
        s.toBack();
    }

    public void removeMessage(Screen s){
        messages.getChildren().remove(s);
    }

    public boolean timePassed(){
        return infos.timePassed();
    }

    public void addInfoObserver(Observer ob){
        infos.addObserver(ob);
    }

}