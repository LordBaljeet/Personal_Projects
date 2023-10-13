package g58744.boulder_dash.View.AppView.View;

import g58744.boulder_dash.Model.Level;
import g58744.boulder_dash.Model.Observable;
import g58744.boulder_dash.Model.Observer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppInfo extends HBox implements Observable {
    private int currentTime;
    private final Label collected_D_Label = new Label();
    private final Label timeLabel = new Label();
    private final Label requiredD = new Label();
    private final Label remainingD = new Label();
    private Timeline timeline;
    private final MediaPlayer player;
    private final List<Observer> observers;
    private final Level lvl;

    private final ProgressIndicator pi;

    public AppInfo(Level lvl) {
        this.lvl = lvl;
        pi = new ProgressIndicator();
        pi.setProgress(0);
        currentTime = lvl.time();
        observers = new ArrayList<>();
        File directory = new File("src/main/resources/SoundEffects");
        File file = Objects.requireNonNull(directory.listFiles())[4];
        Media timer = new Media(file.toURI().toString());
        player = new MediaPlayer(timer);
        player.setAutoPlay(true);
    }

    public void setInfoSection(){
        requiredD.setText(lvl.requiredDiamonds()+"");
        timeLabel.setText(String.format("%03d",lvl.time()));
        remainingD.setText(String.format("%02d",lvl.totalDiamonds()));
        HBox timerContainer = new HBox();
        createInfoBox(timerContainer, timeLabel,"images/clockInfo.png");
        HBox requiredDContainer = new HBox();
        createInfoBox(requiredDContainer, requiredD,"images/doorInfo.png");
        HBox remainingDContainer = new HBox();
        createInfoBox(remainingDContainer, remainingD,"images/diamondInfo.png");
        collected_D_Label.setText(String.format("%02d",0));
        HBox collectedDContainer = new HBox();
        createInfoBox(collectedDContainer, collected_D_Label,pi);
        this.getChildren().addAll(requiredDContainer, remainingDContainer, collectedDContainer, timerContainer);
        this.getStyleClass().add("infos");
        this.setSpacing(64);
        timer();
        translateYProperty().addListener(e->{
            if(getTranslateY() == 0){
                setStyle("-fx-background-color:none;");
            }else{
                setStyle("-fx-background-color:rgba(255,255,255,0.4);");
            }
        });
    }

    private void createInfoBox(HBox box, Label l){
        int nbLabels = l.getText().length();
        for (int i = 0; i < nbLabels; i++) {
            Label l1 = new Label(l.getText().substring(i,i+1));
            l1.getStyleClass().add("infoBox");
            int finalI = i;
            l.textProperty().addListener((obs, oldText, newText)-> l1.setText(newText.substring(finalI,finalI+1)));
            box.getChildren().add(l1);
        }
    }

    private void createInfoBox(HBox box, Label l, Node node){
        VBox nodeBox = new VBox();
        nodeBox.getStyleClass().add("infoBox");
        nodeBox.getChildren().add(node);
        nodeBox.setAlignment(Pos.BOTTOM_CENTER);
        box.getChildren().add(nodeBox);
        createInfoBox(box,l);
    }

    private void createInfoBox(HBox box, Label l, String path){
        VBox nodeBox = new VBox();
        ImageView img = new ImageView(new Image(path));
        nodeBox.getStyleClass().add("infoBox");
        nodeBox.getChildren().add(img);
        box.getChildren().add(nodeBox);
        createInfoBox(box,l);
    }

    public void updateCollectedD(int collectedDiamonds){
        String s = String.format("%02d",collectedDiamonds);
        String sd = String.format("%02d",lvl.totalDiamonds()-collectedDiamonds);
        collected_D_Label.setText(s);
        pi.setProgress((double)collectedDiamonds/lvl.requiredDiamonds());
        remainingD.setText(sd);
    }

    private void timer(){
        if(timeline != null){
            timeline.stop();
        }
        currentTime = lvl.time();
        timeLabel.setText(currentTime+"");
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                        actionEvent -> {
                            currentTime--;
                            timeLabel.setText(String.format("%03d",currentTime));
                            if(currentTime <=0){
                                timeline.stop();
                                notifyObservers();
                            }
                            else if(currentTime <= 5){
                                player.setCycleCount(5);
                                player.play();
                            }
                        }
                ));
        timeline.playFromStart();
    }

    public void resumeTimer(){
        timeline.play();
    }

    public void stopTimer(){
        timeline.stop();
        player.stop();
    }

    public boolean timePassed(){
        return currentTime == 0;
    }

    @Override
    public void addObserver(Observer ob) {
        observers.add(ob);
    }

    @Override
    public void removeObserver(Observer ob) {
        observers.remove(ob);
    }

    @Override
    public void notifyObservers(){
        for (Observer ob :observers) { ob.update();}
    }
}