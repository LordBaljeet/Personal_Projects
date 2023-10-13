package g58744.boulder_dash;

import g58744.boulder_dash.View.AppView.View.AppWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class

Boulder_Dash_App extends Application{

    public static void main(String[] args) {
       Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        AppWindow app = new AppWindow();
        app.start(stage);
    }
}