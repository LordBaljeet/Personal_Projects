package g58744.boulder_dash.View.AppView.View;

import g58744.boulder_dash.Model.Board;
import g58744.boulder_dash.Model.Entities.Entity;
import g58744.boulder_dash.Model.Entities.EntityType;
import g58744.boulder_dash.Model.Entities.Player;
import g58744.boulder_dash.Model.Model;
import g58744.boulder_dash.Model.Observer;
import g58744.boulder_dash.View.ConsoleView.ConsoleView;
import g58744.boulder_dash.View.ConsoleView.View;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.GridPane;

public class AppBoard extends GridPane implements Observer {
    private AppSquare[][] squares;
    private final Board board;
    private Player player;
    private final int style;

    public AppBoard(Board board, int style) {
        this.board = board;
        this.style = style;
        board.addObserver(this);
        createBoard();
    }

    public void createBoard() {
        Entity[][] brd = board.getBoard();
        squares = new AppSquare[brd.length][brd[0].length];
        for (int y = 0; y < brd.length; y++) {
            for (int x = 0; x < brd[0].length; x++) {
                if(brd[y][x] instanceof Player) player = (Player) brd[y][x];
                squares[y][x] = new AppSquare(new AppEntity(brd[y][x].getType(), style));
                add(squares[y][x],x,y);
            }
        }
    }

    @Override
    public void update(){
        Entity[][] brd = board.getBoard();
        for (int y = 0; y < brd.length; y++) {
            for (int x = 0; x < brd[0].length; x++) {
                if(squares[y][x].getComponent().getComponentType() != brd[y][x].getType()){
                    squares[y][x].setComponent(new AppEntity(brd[y][x].getType(), style));
                }
            }
        }
        if(player.isDead()){
            squares[player.getPos().y()][player.getPos().x()].getChildren().add(new AppEntity(EntityType.DEAD, style));
        }
        else {
            if(player.isGoingLeft()){
                squares[player.getPos().y()][player.getPos().x()].setComponent(new AppEntity(EntityType.PLAYERL,style));
            }
            else {
                squares[player.getPos().y()][player.getPos().x()].setComponent(new AppEntity(EntityType.PLAYERR,style));
            }
        }
    }
}