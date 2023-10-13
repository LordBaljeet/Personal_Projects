package g58744.boulder_dash.View.ConsoleView;

public interface View {
    /**
     * displays the board
     */
    void displayBoard();

    /**
     * displays the number of totalDiamonds collected out of required totalDiamonds
     */
    void displayDiamondsCount();

    /**
     * displays the commands the user can use
     */
    void displayCommands();

    /**
     * displays a win message
     */
    void displayWin();

    /**
     * displays a lose message
     */
    void displayLose();

    /**
     * asks the player for the next move or next command
     * @return the move direction or command
     */
    String askCommand();

    /**
     * displays a death message
     */
    void displayDeath();

    /**
     * displays a welcome message
     */
    void displayWelcome();

    /**
     * asks if the user wants to restart
     * @return  true if he wants to restart, false otherwise
     */
    boolean askRestart();

    int askLevel();

}
