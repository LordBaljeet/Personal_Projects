package g58744.boulder_dash.View.AppView.View.Screens;

import g58744.boulder_dash.Model.Game;
import g58744.boulder_dash.Model.LevelsManager;
import g58744.boulder_dash.Model.Model;
import g58744.boulder_dash.View.AppView.Controller.AppController;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomePage extends HBox {

    private final double height;
    private final double width;
    private final LevelsManager mgr;
    private final GridPane playGrid;
    private final GridPane settingsGrid;
    private final List<File> songs;
    private int currentStyle = 0;
    private int chosenStyle = 0;
    private MediaPlayer player;
    private final TranslateTransition tt;
    public HomePage(double height, double width) {
        setPrefSize(2*width,height);
        this.width = width;
        this.height = height;
        tt = new TranslateTransition(Duration.seconds(.5),this);
        tt.setToX(0);
        songs = new ArrayList<>();
        mgr = new LevelsManager();
        setAlignment(Pos.CENTER);
        playGrid = createGrid();
        settingsGrid = createGrid();
        getChildren().addAll(playGrid,settingsGrid);
        setAlignment(Pos.TOP_LEFT);
        createPlayScreen();
        playMusic();
        createSettingsScreen();
    }

    private GridPane createGrid(){
        GridPane playGrid = new GridPane();
        playGrid.setPrefSize(width,height);
        playGrid.setMaxSize(width,height);
        playGrid.setMinSize(width,height);
        playGrid.getStyleClass().add("homePageGrid");

        //Columns:
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(66);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(16);
        ColumnConstraints c3 = new ColumnConstraints();
        c3.setPercentWidth(16);

        playGrid.getColumnConstraints().addAll(c1,c2,c3);

        //Rows:
        RowConstraints r1 = new RowConstraints();
        r1.setPercentHeight(16);
        RowConstraints r2 = new RowConstraints();
        r2.setPercentHeight(66);
        RowConstraints r3 = new RowConstraints();
        r3.setPercentHeight(16);

        playGrid.getRowConstraints().addAll(r1,r2,r3);

        return playGrid;
    }

    private void createPlayScreen(){

        //Title:
        BorderPane titleBox = new BorderPane();
        Label title = new Label("BOULDER DASH");
        Button exit = new Button("X");
        exit.setOnAction(e-> Platform.exit());
        exit.getStyleClass().add("exitBtn");
        BorderPane.setAlignment(exit,Pos.CENTER);
        titleBox.setCenter(title);
        titleBox.setLeft(exit);
        playGrid.add(titleBox,0,0,3,1);

        //Image
        HBox imgContainer = new HBox();
        imgContainer.getStyleClass().add("playGridImage");
        playGrid.add(imgContainer,0,1);

        //playBtn:
        HBox playBtnContainer = new HBox();
        Button playBtn = new Button("PLAY");
        playBtn.getStyleClass().add("playGridPB");
        ChoiceBox<Integer> level = new ChoiceBox<>();
        for (int i = 0; i < mgr.size(); i++) {
            level.getItems().add(i);
        }
        level.setValue(level.getItems().get(0));
        playBtn.setOnAction(e->playHandler(level.getValue()));
        playBtnContainer.setAlignment(Pos.CENTER);
        playBtnContainer.getChildren().addAll(playBtn,level);
        playGrid.add(playBtnContainer,0,2);

        //PrevBtn:
        HBox prevBtnContainer = new HBox();
        prevBtnContainer.setAlignment(Pos.CENTER);
        Button prevBtn = new Button("→");
        prevBtn.setOnAction(this::slidingHandler);
        prevBtnContainer.getChildren().add(prevBtn);
        playGrid.add(prevBtnContainer,1,2,2,1);

        //controls
        VBox infoContainer = new VBox();
        infoContainer.setSpacing(20);
        infoContainer.getChildren().addAll(
                createControl("Move","↑","↓","←","→"),
                createControl("Undo","Ctrl-Z"),
                createControl("Redo","Ctrl-Y"),
                createControl("Menu","Esc"),
                createControl("Restart","R")
        );
        infoContainer.setAlignment(Pos.CENTER);
        playGrid.add(infoContainer,1,1,2,1);

        for (Node item : playGrid.getChildren() ) {
            item.getStyleClass().add("gridItem");
        }
    }

    private void createSettingsScreen(){
        //Title:
        BorderPane titleBox = new BorderPane();
        Label title = new Label("BOULDER DASH");
        Button exit = new Button("X");
        exit.setOnAction(e-> Platform.exit());
        exit.getStyleClass().add("exitBtn");
        BorderPane.setAlignment(exit,Pos.CENTER);
        titleBox.setCenter(title);
        titleBox.setLeft(exit);
        settingsGrid.add(titleBox,0,0,3,1);

        //saveBtn:
        HBox saveBtnContainer = new HBox();
        Button saveBtn = new Button("SAVE");
        saveBtn.setOnAction(e->chosenStyle = currentStyle);
        saveBtnContainer.setAlignment(Pos.CENTER);
        saveBtnContainer.getChildren().add(saveBtn);
        settingsGrid.add(saveBtnContainer,0,2);

        //PrevBtn:
        HBox prevBtnContainer = new HBox();
        prevBtnContainer.setAlignment(Pos.CENTER);
        Button prevBtn = new Button("←");
        prevBtn.setOnAction(this::slidingHandler);
        prevBtnContainer.getChildren().add(prevBtn);
        settingsGrid.add(prevBtnContainer,1,2,2,1);

        //Settings:
            //Slider:
        VBox settingsContainer = new VBox();
        VBox sliderContainer = new VBox();
        Slider volume = new Slider();
        volume.getStyleClass().add("volumeSlider");
        volume.setPrefWidth((width/6)-10);
        volume.setOrientation(Orientation.HORIZONTAL);
        volume.setValue(25);
        volume.setShowTickLabels(true);
        volume.setMajorTickUnit(25);
        player.setVolume(volume.getValue()/100);
        volume.valueProperty().addListener((observableValue, number, t1) -> player.setVolume(volume.getValue()/100));
        Label sliderLabel = new Label("Adjust Volume:");
        sliderContainer.getChildren().addAll(sliderLabel,volume);
        settingsContainer.getChildren().add(sliderContainer);
        settingsContainer.getStyleClass().add("settingsContainer");
        settingsGrid.add(settingsContainer,1,1,2,1);

            //Styles
        HBox styleContainer = new HBox();
        HBox style = new HBox();
        style.getStyleClass().add("square");
        style.setMaxSize(640,64);
        style.setMinSize(640,64);
        Button prevStyle = new Button("<");
        prevStyle.setOnAction(e->prevStyleHandler(style));
        Button nextStyle = new Button(">");
        nextStyle.setOnAction(e->nextStyleHandler(style));
        styleContainer.getChildren().addAll(prevStyle,style,nextStyle);
        styleContainer.setAlignment(Pos.CENTER);
        styleContainer.setMinHeight(64);
        settingsGrid.add(styleContainer,0,1);

        for (Node item : settingsGrid.getChildren() ) {
            item.getStyleClass().add("gridItem");
        }
    }

    private BorderPane createControl(String label, String... keyLabel){
        BorderPane control = new BorderPane();
        HBox keyBox = new HBox();
        keyBox.setAlignment(Pos.BASELINE_CENTER);
        keyBox.getStyleClass().add("keyBox");
        Label text = new Label(label);
        BorderPane.setAlignment(text,Pos.CENTER);
        for (String keyL : keyLabel) {
            Button key = new Button(keyL);
            keyBox.getChildren().add(key);
        }
        control.setLeft(keyBox);
        control.setRight(text);
        control.getStyleClass().add("control");
        return control;
    }

    private void playHandler(int index){
        Model model = new Game();
        AppController controller = new AppController(model);
        controller.play(getScene(), index, chosenStyle);
        controller.setMediaPlayer(player);
    }

    private void playMusic(){
        File directory = new File("src/main/resources/SoundEffects");
        File[] files = directory.listFiles();
        if(files != null){
            Collections.addAll(songs, files);
            Media music = new Media(songs.get(3).toURI().toString());
            player = new MediaPlayer(music);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.play();
        }
    }

    private void slidingHandler(ActionEvent e){
        if(tt.getToX() == 0) {
            tt.setToX(-width);
        }else{
            tt.setToX(0);
        }
        tt.play();
    }

    private void prevStyleHandler(HBox container){
        if(currentStyle > 0){
            currentStyle--;
            container.setStyle("-fx-background-position: 0 -"+64*currentStyle);
        }
    }

    private void nextStyleHandler(HBox container){
        if(currentStyle < 5){
            currentStyle++;
            container.setStyle("-fx-background-position: 0 -"+64*currentStyle);
        }
    }
}