package g58744.boulder_dash;

import g58744.boulder_dash.View.ConsoleView.ConsoleView;
import g58744.boulder_dash.Controller.ConsoleController;
import g58744.boulder_dash.Model.Game;
import g58744.boulder_dash.Model.Model;

public class Boulder_Dash_Console {

    public static void main(String[] args){
        Model game = new Game();
        ConsoleView view = new ConsoleView(game);
        ConsoleController ctrl = new ConsoleController(game, view);
        ctrl.play();
    }
}