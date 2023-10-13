package g58744.boulder_dash.View.ConsoleView;

import g58744.boulder_dash.Model.Entities.Entity;
import g58744.boulder_dash.Model.Board;
import g58744.boulder_dash.Model.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConsoleView implements View{
    private final Model game;
    private final Scanner scan;

    public ConsoleView(Model game) {
        this.game = game;
        scan = new Scanner(System.in);
    }

    @Override
    public void displayBoard() {
        Board board = game.getBoard();

        Entity[][] boardComps = board.getBoard();
        StringBuilder levelString = new StringBuilder();

        for (Entity[] boardComp : boardComps) {
            for (Entity comp :
                    boardComp) {
                switch (comp.getType()) {
                    case ROCK, BORDER -> {
                        levelString.append(TerminalBackground.RED).append(comp).append(TerminalBackground.RESET);
                    }
                    case DOOR -> levelString.append(TerminalBackground.GREEN).append(comp).append(TerminalBackground.RESET);
                    case DIAMOND -> levelString.append(TerminalBackground.CYAN).append(comp).append(TerminalBackground.RESET);
                    case WALL -> levelString.append(TerminalBackground.BLUE).append(comp).append(TerminalBackground.RESET);
                    case VOID -> levelString.append("   ");
                    case CHARACTER -> levelString.append(TerminalBackground.YELLOW).append(comp).append(TerminalBackground.RESET);
                    default -> levelString.append(TerminalBackground.PURPLE).append(comp).append(TerminalBackground.RESET);
                }
            }
            levelString.append("\n");
        }

        System.out.println(levelString);
    }

    @Override
    public void displayDiamondsCount() {
        System.out.println("Diamonds: "+game.getCollectedDiamonds() + "/" + game.getRequiredDiamonds());
    }

    @Override
    public void displayCommands() {
        String[] commands = {
                "\t|-> W to move Up",
                "\t|-> a to move Left",
                "\t|-> s to move Down",
                "\t|-> d to move Right",
                "\t|-> r to restart",
                "\t|-> exit to exit the game "};
        int stringLength = commands[0].length();
        for (String s : commands) {
            if (s.length() > stringLength) {
                stringLength = s.length();
            }
        }
        String msg = "C O M M A N D S";
        String borders = TerminalColor.PURPLE + "\t|"+"-".repeat(stringLength-2) + "|" + TerminalColor.RESET;
        System.out.println(borders);
        System.out.println(TerminalColor.PURPLE + "\t|"+" ".repeat((stringLength-msg.length())/2) + msg+" ".repeat((stringLength-msg.length())/2-2)+"|" + TerminalColor.RESET);
        System.out.println(borders);

        String cmdBorders = TerminalColor.CYAN + "\t|"+"-".repeat(stringLength-2) + "|" + TerminalColor.RESET;
        for (String cmd : commands) {
            System.out.println(TerminalColor.CYAN +cmd+" ".repeat(stringLength-cmd.length())+"|"+ TerminalColor.RESET);
        }
        System.out.println(cmdBorders+"\n");
    }

    @Override
    public void displayWin() {
        String msg = "Y O U  W I N  B O Z O !";
        String borders = TerminalColor.GREEN + "|"+"-".repeat(msg.length()) + "|" + TerminalColor.RESET;
        System.out.println(borders);
        System.out.println(TerminalColor.GREEN + "|"+msg+"|"+ TerminalColor.RESET);
        System.out.println(borders);
    }

    @Override
    public void displayLose() {
        String msg = "Y O U  L O S E  B O Z O !";
        String borders = TerminalColor.RED + "|"+"-".repeat(msg.length()) + "|" + TerminalColor.RESET;
        System.out.println(borders);
        System.out.println(TerminalColor.RED + "|"+msg+"|"+ TerminalColor.RESET);
        System.out.println(borders);
    }

    @Override
    public String askCommand() {
        String command = "";
        List<String> commands = new ArrayList<>(Arrays.asList("move","undo","redo","w","a","s","d","restart","abandon"));
        while(!commands.contains(command)) {
            System.out.println("Enter your next move direction or a command[restart|undo|redo|abandon]");
            System.out.print("> ");
            command = scan.nextLine().toLowerCase();
        }
        return command;
    }

    @Override
    public void displayDeath() {
        String msg = "Y O U  D I E D !";
        String borders = TerminalColor.RED + "|"+"-".repeat(msg.length()) + "|" + TerminalColor.RESET;
        System.out.println(borders);
        System.out.println(TerminalColor.RED + "|"+msg+"|"+ TerminalColor.RESET);
        System.out.println(borders);
    }

    @Override
    public void displayWelcome() {
        String msg = "W E L C O M E  T O  B O U L D E R  D A S H !";
        String borders = TerminalColor.PURPLE + "\t\t\t\t\t\t\t\t|"+"-".repeat(msg.length()) + "|" + TerminalColor.RESET;
        System.out.println(borders);
        System.out.println(TerminalColor.PURPLE + "\t\t\t\t\t\t\t\t|"+msg+"|" + TerminalColor.RESET);
        System.out.println(borders+"\n");
    }

    @Override
    public boolean askRestart() {
        String msg = "R E S T A R T ?";
        String borders = TerminalColor.YELLOW + "|"+"-".repeat(msg.length()) + "|" + TerminalColor.RESET;
        List<String> responses = new ArrayList<>(Arrays.asList("no","n","yes","y"));
        String response;
        do{
            System.out.println(borders);
            System.out.println(TerminalColor.YELLOW + "|"+msg+"|" + TerminalColor.RESET);
            System.out.println(borders);
            String answer = " Yes(y), No(n) ";
            System.out.println("|"+answer+"|");
            System.out.println("|"+"-".repeat(answer.length()) + "|");
            System.out.print("> ");
            response = scan.nextLine().toLowerCase();

        }while(!responses.contains(response));
        return !response.equals("no") && !response.equals("n");
    }

    @Override
    public int askLevel() {
        int lvl;
        do{
            System.out.print("Enter desired level: ");
            lvl = scan.nextInt();
        }while(lvl < 0 || lvl > game.nbOfLevels());
        return lvl;
    }

    /**
     * To add some spice to the output
     */
    private enum TerminalColor {
        RED("\u001B[31m"), BLUE("\u001B[34m"), GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"), PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"), RESET("\u001B[0m");

        private final String color;

        TerminalColor(String color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return color;
        }

    }
    private enum TerminalBackground {
        RED("\u001B[41m"), BLUE("\u001B[44m"), GREEN("\u001B[42m"),
        YELLOW("\u001B[43m"), PURPLE("\u001B[45m"),
        CYAN("\u001B[46m"), RESET("\u001B[0m");

        private final String color;

        TerminalBackground(String color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return color;
        }

    }

}
