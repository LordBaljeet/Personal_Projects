package g58744.boulder_dash.Controller;

import g58744.boulder_dash.Model.Entities.Player;
import g58744.boulder_dash.View.ConsoleView.ConsoleView;
import g58744.boulder_dash.Model.Direction;
import g58744.boulder_dash.Model.Model;
import g58744.boulder_dash.Model.Position;

public class ConsoleController {
    private final Model game;
    private final ConsoleView view;

    public ConsoleController(Model game, ConsoleView view) {
        this.game = game;
        this.view = view;
    }

    /**
     * starts the game and controls it
     */
    public void play() {
        boolean restart;
        boolean won;
        boolean hasNext = false;
        do {
            view.displayWelcome();
            view.displayCommands();
            int currentLevel = view.askLevel();
            game.start(currentLevel);
            Player player = game.getPlayer();
            game.hideDoor();
            view.displayBoard();
            view.displayDiamondsCount();
            do {
                Position destination;
                Direction dir = null;
                String command = view.askCommand();
                switch (command) {
                    case "undo" -> {
                        game.undo();
                        view.displayBoard();
                        view.displayDiamondsCount();
                    }
                    case "redo" -> {
                        game.redo();
                        view.displayBoard();
                        view.displayDiamondsCount();
                    }
                    case "w" -> dir = Direction.N;
                    case "a" -> dir = Direction.W;
                    case "s" -> dir = Direction.S;
                    case "d" -> dir = Direction.E;
                    case "restart" -> {
                        game.start(currentLevel);
                        player = game.getPlayer();
                        game.hideDoor();
                        view.displayBoard();
                        view.displayDiamondsCount();
                    }
                    default  -> game.abandonGame();
                }
                if (!game.GameEnded() && dir != null) {
                    destination = player.getPos().moveTo(dir);
                    if(game.isPossibleMove(destination)){
                        game.move(player, destination);
                        if (game.getCollectedDiamonds() == game.getRequiredDiamonds()) {
                            game.showDoor();
                        }
                        if (player.isDead()) {
                            view.displayBoard();
                            view.displayDiamondsCount();
                            view.displayDeath();
                        } else {
                            view.displayBoard();
                            view.displayDiamondsCount();

                        }
                    }
                }
            } while (!game.GameEnded());
            restart = false;
            won = game.gameWon();
            if (won) {
                view.displayWin();
                hasNext = game.hasNextLevel();
                if(hasNext) currentLevel++;
            }
            else {
                view.displayLose();
                restart = view.askRestart();
            }

        } while (restart || (won && hasNext));

    }

}