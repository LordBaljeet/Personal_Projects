package g58744.boulder_dash.View.AppView.Controller;

import g58744.boulder_dash.Model.Observer;
import g58744.boulder_dash.View.AppView.Model.ScreenKey;
import g58744.boulder_dash.View.AppView.View.AppGame;
import g58744.boulder_dash.Model.Entities.Player;
import g58744.boulder_dash.Model.Direction;
import g58744.boulder_dash.Model.Model;
import g58744.boulder_dash.Model.Position;
import g58744.boulder_dash.View.AppView.View.Screens.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.io.File;
import java.util.*;

public class AppController implements Observer {

    /*======================*/
    /*      Attributes      */
    /*======================*/
    private final Model game;
    private AppGame appGame;
    private Player player;
    private Scene scene;
    private final Map<ScreenKey, Screen> screens;
    private TranslateTransition tt;
    private int currentLevel;
    private final int imgSize = 64;
    private int boardWidth;
    private int boardHeight;
    private int style;
    private final List<File> songs;
    private MediaPlayer p;

    /*======================*/
    /*      Constructor     */
    /*======================*/

    public AppController(Model game) {
        this.game = game;
        screens = new HashMap<>();
        songs = new ArrayList<>();
    }

    /*==============================*/
    /*      Functional Methods      */
    /*==============================*/

    public void play(Scene scene, int index, int style) {
        this.style = style;
        this.scene = scene;
        currentLevel = index;
        appGame = new AppGame(game, style);
        start();
        boardWidth = game.getBoard().getBoard()[0].length;
        boardHeight = game.getBoard().getBoard().length;
        setTransition();
        appGame.stopTimer();
        showBoard();
        scene.setRoot(appGame);
        playMusic();
    }

    private void movementHandler(KeyEvent e){

        KeyCombination undoKeys = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
        KeyCombination redoKeys = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);

        if(undoKeys.match(e)){
            game.undo();
            player = game.getPlayer();
            manageCameraTransitions(player.getPos());
            appGame.updateInfos();
        }
        else if(redoKeys.match(e)){
            game.redo();
            player = game.getPlayer();
            manageCameraTransitions(player.getPos());
            appGame.updateInfos();
        }
        else {
            KeyCode code = e.getCode();
            Position destination = player.getPos();
            switch (code) {
                case LEFT -> {
                    destination = destination.moveTo(Direction.W);
                    movePlayer(destination);
                }
                case RIGHT -> {
                    destination = destination.moveTo(Direction.E);
                    movePlayer(destination);
                }
                case UP -> {
                    destination = destination.moveTo(Direction.N);
                    movePlayer(destination);
                }
                case DOWN -> {
                    destination = destination.moveTo(Direction.S);
                    movePlayer(destination);
                }
                case ESCAPE -> {
                    EscapeScreen es = (EscapeScreen) screens.get(ScreenKey.ESCAPESCREEN);
                    es.setVisible(!es.isVisible());
                    if(es.isVisible()) {
                        appGame.stopTimer();
                    }
                    else appGame.resumeTimer();
                }
                case R -> restartHandler.handle(null);
                default -> {
                }
            }
        }
    }

    private void movePlayer(Position destination){
        if(game.getBoard().isInsideBoard(destination) && !game.GameEnded()) {
            Player player = game.getBoard().getPlayer();
            int oldDCount = game.getCollectedDiamonds();
            if(!game.isPossibleMove(destination)) {
                String msg = "You can't move there!";
                Message es = new Message(msg,true);
                appGame.addMessage(es);
                fadeMessage(es);
            }else{
                game.move(player, destination);
                manageCameraTransitions(player.getPos());
                int currentDCount = game.getCollectedDiamonds();
                if (oldDCount != currentDCount && currentDCount == game.getRequiredDiamonds()) {
                    String msg = "Door opened!";
                    Message doorMsg = new Message(msg,false);
                    appGame.addMessage(doorMsg);
                    fadeMessage(doorMsg);
                    game.showDoor();
                }else if(currentDCount < game.getRequiredDiamonds()){
                    game.hideDoor();
                }
            }
            appGame.updateInfos();
        }
    }

    EventHandler<KeyEvent> handler = new EventHandler<>() {
        @Override
        public void handle(KeyEvent e) {
            movementHandler(e);
            if(player.isDead()){
                Media bonk = new Media(songs.get(2).toURI().toString());
                MediaPlayer mdp = new MediaPlayer(bonk);
                mdp.play();
                appGame.stopTimer();
                appGame.setScreen(screens.get(ScreenKey.DEATHSCREEN));
                scene.removeEventHandler(KeyEvent.KEY_PRESSED, this);
            }
            if(game.gameWon()){
                appGame.stopTimer();
                appGame.addMessage(screens.get(ScreenKey.WINSCREEN));
                scene.removeEventHandler(KeyEvent.KEY_PRESSED, this);
            }
        }
    };

    EventHandler<ActionEvent> restartHandler = new EventHandler<>() {
        @Override
        public void handle(ActionEvent e) {
            appGame.getChildren().remove(screens.get(ScreenKey.DEATHSCREEN));
            start();
            centerOnPlayer();
        }
    };

    EventHandler<ActionEvent> abandonHandler = new EventHandler<>() {
        @Override
        public void handle(ActionEvent e) {
            tt.setToX(0);
            tt.setToY(0);
            tt.play();
            p.stop();
            scene.setRoot(new HomePage(scene.getHeight(),scene.getWidth()));
        }
    };

    EventHandler<ActionEvent> nextGameHandler = new EventHandler<>() {
        @Override
        public void handle(ActionEvent e) {
            if(game.hasNextLevel()){
                tt.setToX(0);
                tt.setToY(0);
                play(scene,++currentLevel, style);
            }
        }
    };

    EventHandler<ActionEvent> homeHandler = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            tt.setToX(0);
            tt.setToY(0);
            p.stop();
            scene.setRoot(new HomePage(scene.getHeight(),scene.getWidth()));
        }
    };

    EventHandler<ActionEvent> exitHandler = event -> Platform.exit();

    private void start(){
        game.start(currentLevel);
        appGame.start(game);
        createScreens();
        appGame.setScreen(screens.get(ScreenKey.ESCAPESCREEN));
        player = game.getBoard().getPlayer();
        appGame.addInfoObserver(this);
    }

    @Override
    public void update() {
        if(appGame.timePassed()){
            appGame.setScreen(screens.get(ScreenKey.TIMEPASSEDSCREEN));
        }
    }

    /*==============================*/
    /*      Aesthetic Methods      */
    /*==============================*/

    private void setTransition(){
        tt = new TranslateTransition();
        tt.setDuration(Duration.seconds(1));
        tt.setNode(appGame);
        tt.setToX(0);
        tt.setToY(0);
    }

    private void manageCameraTransitions(Position pos){
        double screenWidth = scene.getWidth();
        double screenHeight = scene.getHeight();
        if(pos.x() == (int)(screenWidth/imgSize)-3 && tt.getToX()==0) {
            tt.stop();
            tt.setToX(-((boardWidth-(screenWidth/imgSize))*imgSize));
            tt.play();
        }
        else if(pos.x() == (int)(boardWidth-(screenWidth/imgSize))+3 && tt.getToX() < 0) {
            tt.stop();
            tt.setToX(0);
            tt.play();
        }
        else if(pos.y() == (int)(screenHeight/imgSize)-3 && tt.getToY() == 0){
            tt.stop();
            tt.setToY(-((boardHeight-(screenHeight/imgSize))*imgSize));
            tt.play();
        }
        else if(pos.y() == (int)(boardHeight-(screenHeight/imgSize))+3 && tt.getToY() < 0){
            tt.stop();
            tt.setToY(0);
            tt.play();
        }
    }

    private void createScreens(){
        DeathScreen ds = new DeathScreen(scene.getWidth(), scene.getHeight());
        ds.setHandlers(restartHandler, abandonHandler);
        screens.put(ScreenKey.DEATHSCREEN, ds);

        WinScreen ws = new WinScreen(400, 200, game.hasNextLevel());
        ws.setHandlers(nextGameHandler, abandonHandler);
        screens.put(ScreenKey.WINSCREEN, ws);

        EscapeScreen es = new EscapeScreen(scene.getWidth(), scene.getHeight());
        es.setHandlers(homeHandler, exitHandler);
        screens.put(ScreenKey.ESCAPESCREEN,es);

        TimePassedScreen ts = new TimePassedScreen(scene.getWidth(),scene.getHeight());
        ts.setHandlers(restartHandler,abandonHandler);
        screens.put(ScreenKey.TIMEPASSEDSCREEN,ts);
    }

    private void centerOnPlayer(){
        appGame.stopTimer();
        Position pos = player.getPos();
        double screenWidth = scene.getWidth();
        double screenHeight = scene.getHeight();

        if(pos.x() <= (screenWidth/imgSize)-3 && pos.y() <= (screenHeight/imgSize)-3) {
            tt.setToX(0);
            tt.setToY(0);
            tt.play();
        }
        else if(pos.x() >= (screenWidth/imgSize)-3 && pos.y() <= (screenHeight/imgSize)-3) {
            tt.setToX(-((boardWidth-(screenWidth/imgSize))*imgSize));
            tt.setToY(0);
            tt.play();
        }
        else if(pos.x() <= screenWidth/imgSize -3){
            tt.setToX(0);
            tt.setToY(-((boardHeight-(screenHeight/imgSize))*imgSize));
            tt.play();
        }
        else {
            tt.setToX(-((boardWidth-(screenWidth/imgSize))*imgSize));
            tt.setToY(-((boardHeight-(screenHeight/imgSize))*imgSize));
            tt.play();
        }
        tt.setOnFinished(actionEvent -> {
            appGame.resumeTimer();
            scene.addEventHandler(KeyEvent.KEY_PRESSED, handler);
        });
    }

    private void playMusic(){
        File directory = new File("src/main/resources/SoundEffects");
        File[] files = directory.listFiles();
        if(files != null){
            Collections.addAll(songs, files);
        }
    }

    private void showBoard(){
        SequentialTransition st = new SequentialTransition();
        st.getChildren().addAll(centerOn(game.getDoorPos()),centerOn(player.getPos()));
        st.play();
        st.setOnFinished(ActionEvent ->{
            st.stop();
            st.getChildren().clear();
            appGame.resumeTimer();
            game.hideDoor();
            scene.addEventHandler(KeyEvent.KEY_PRESSED, handler);
            appGame.updateBoard();
        });

    }

    private TranslateTransition centerOn(Position pos){
        double screenWidth = scene.getWidth();
        double screenHeight = scene.getHeight();
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1),appGame);
        transition.setDelay(Duration.seconds(1));
        if(pos.x() <= (screenWidth/imgSize)-3&& pos.y() <= (screenHeight/imgSize)-3 && (transition.getToY() != 0 || transition.getToX() != 0)) {
            transition.setToX(0);
            transition.setToY(0);
        }
        else if(pos.x() >= (screenWidth/imgSize)-3 && pos.y() <= (screenHeight/imgSize)-3 &&(transition.getToX() == 0 || transition.getToY() != 0) ) {
            transition.setToX(-((boardWidth-(screenWidth/imgSize))*imgSize));
            transition.setToY(0);
        }
        else if(pos.x() <= screenWidth/imgSize -3 && transition.getToX() != 0 && (transition.getToY() == 0 || transition.getToX() != 0)){
            transition.setToX(0);
            transition.setToY(-((boardHeight-(screenHeight/imgSize))*imgSize));
        }
        else {
            transition.setToX(-((boardWidth-(screenWidth/imgSize))*imgSize));
            transition.setToY(-((boardHeight-(screenHeight/imgSize))*imgSize));
        }
        tt.setToX(transition.getToX());
        tt.setToY(transition.getToY());
        return transition;
    }

    private void fadeMessage(Screen s){
        FadeTransition ft = new FadeTransition(Duration.seconds(5),s);
        ft.setToValue(0);
        ft.play();
        ft.setOnFinished(ActionEvent -> appGame.removeMessage(s));
    }

    public void setMediaPlayer(MediaPlayer p){
        this.p = p;
    }
}