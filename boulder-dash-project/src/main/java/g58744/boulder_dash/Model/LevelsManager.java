package g58744.boulder_dash.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LevelsManager {
    private final List<Level> levelsList;

    public LevelsManager() {
        this.levelsList = new ArrayList<>();
        createLevels();
    }

    /**
     * Create The game Levels
     * {/} for borders
     * {=} for dirt
     * {+} for totalDiamonds
     * {*} for rocks
     * {_} for empty squares
     * {?} for door
     * {$} for player
     * {|} for walls
     */
    private void createLevels() {
        File directory = new File("src/main/java/g58744/boulder_dash/Model/Levels");
        int fileCount = Objects.requireNonNull(directory.list()).length;

        BufferedReader fileReader;

        for (int i = 0; i < fileCount; i++) {
            String line;
            int diamonds = 0;
            List<String> levelLayout = new ArrayList<>();

            try{
                FileReader file = new FileReader("src/main/java/g58744/boulder_dash/Model/Levels/"+
                        Objects.requireNonNull(directory.list())[i]);
                fileReader = new BufferedReader(file);

                while((line = fileReader.readLine()) != null) {
                    levelLayout.add(line);
                    for (char c :
                            line.toCharArray()) {
                        if (c == '+') {
                            diamonds++;
                        }
                    }
                }
                int requiredDiamonds = Integer.parseInt(levelLayout.get(0));
                int time = Integer.parseInt(levelLayout.get(1));
                levelLayout.remove(0);
                levelLayout.remove(0);
                Level level = new Level(diamonds, requiredDiamonds, time,levelLayout);
                levelsList.add(level);
            } catch (FileNotFoundException e) {
                System.out.println("File not found!");
            } catch (IOException e) {
                System.out.println("Error Reading file");
            }
        }
    }

    public Level getLevel(int index) {
        if(index < 0 || index > levelsList.size()) {
            throw new IllegalArgumentException("Level does not exist!");
        }
        return levelsList.get(index);
    }

    public int size(){
        return levelsList.size();
    }
}