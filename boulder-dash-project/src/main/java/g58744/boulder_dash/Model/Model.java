package g58744.boulder_dash.Model;

import g58744.boulder_dash.Model.Entities.MovableEntity;
import g58744.boulder_dash.Model.Entities.Player;

import java.util.List;

public interface Model {
    /**
     * Moves the object from start Position to destination Position
     * @param source the source of the movement
     * @param destination the destination to move to
     */
    void move(MovableEntity source, Position destination);

    Player getPlayer();

    /**
     * Hides the door
     */
    void hideDoor();

    /**
     * Shows the door
     */
    void showDoor();

    /**
     * Verifies if the move is possible
     * @param destination the destination of the move
     * @return true if possible, false otherwise
     */
    boolean isPossibleMove(Position destination);

    /**
     * Manages falling objects such as rocks or totalDiamonds
     */
    List<MoveData> manageFallingComponents();

    /**
     * Pushes rocks that can be currently pushed
     * @param destination the destination of the player/position of potential rock
     * @param dir the direction of the player movement
     * @return the move data if a rock was pushed or null otherwise.
     */
    MoveData managePushingBehaviour(Position destination, Direction dir);

    /**
     * sets data for the current level
     * @param levelIndex the level to start the game with
     */
    void start(int levelIndex);

    /**
     * checks if the game ended
     * @return true if the game ended, false otherwise
     */
    boolean GameEnded();

    /**
     * Abandon the game, thus ending it
     */
    void abandonGame();

    /**
     * undo move
     */
    void undo();

    /**
     * redo move
     */
    void redo();

    /**
     * checks if the player won the game
     * @return true if the player won, false otherwise
     */
    boolean gameWon();

    /**
     *
     * @return the number of totalDiamonds collected
     */
    int getCollectedDiamonds();

    /**
     *
     * @return the number of required totalDiamonds
     */
    int getRequiredDiamonds();

    /**
     *
     * @return the position of the door
     */
    Position getDoorPos();

    /**
     *
     * @return the board
     */
    Board getBoard();

    /**
     * sets the next level
     * @return true if the operation succeeded, false otherwise
     */
    boolean hasNextLevel();

    /**
     *
     * @return the current level
     */
    Level getCurrentLvl();

    int nbOfLevels();

}
