package g58744.boulder_dash.Model;

import g58744.boulder_dash.Model.Entities.*;
import java.util.ArrayList;
import java.util.List;

public class Board implements Observable{
    private Entity[][] board;

    private final List<Observer> observers = new ArrayList<>();
    private Player player;

    /**
     * Creates a new board with the given level
     * @param levelLayout the level layout
     */
    public void createBoard(List<String> levelLayout) {
        board = new Entity[levelLayout.size()][levelLayout.get(0).length()];
        for (int y = 0; y < levelLayout.size(); y++) {
            for (int x = 0; x < levelLayout.get(0).length(); x++) {
                char comp = levelLayout.get(y).charAt(x);
                Position pos = new Position(x,y);
                Entity entity = switch (comp) {
                    case '*' -> new DeadlyEntity(EntityType.ROCK,pos);
                    case '?' -> new Entity(EntityType.DOOR);
                    case '+' -> new DeadlyEntity(EntityType.DIAMOND,pos);
                    case '|' -> new Entity(EntityType.WALL);
                    case '/' -> new Entity(EntityType.BORDER);
                    case '_' -> new Entity(EntityType.VOID);
                    case '$' -> player = new Player(new Position(x,y));
                    default -> new Entity(EntityType.DIRT);
                };

                board[y][x] = entity;
            }
        }
    }

    /**
     * gets a component of the board by its position
     * @param pos the position of the component
     * @return the component
     */
    public Entity getEntity(Position pos) {
        return board[pos.y()][pos.x()];
    }

    /**
     * replaces a component with another
     * @param pos the position of the component to replace
     * @param comp the component to replace with
     */
    public void replaceEntity(Position pos, Entity comp) {
        board[pos.y()][pos.x()] = comp;
        notifyObservers();
    }

    /**
     *
     * @return the board
     */
    public Entity[][] getBoard() {
        return board;
    }

    /**
     * checks if the given position is inside the board
     * @param pos the position to check
     * @return true if its inside, false otherwise
     */
    public boolean isInsideBoard(Position pos) {
        return pos.x() < board[0].length && pos.x() >= 0 && pos.y() < board.length && pos.y() >= 0;
    }

    /**
     * finds the position of the door
     * @return the position of the door
     */
    public Position findDoor() {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                Position doorPos = new Position(x,y);
                if(getEntity(doorPos).getType() == EntityType.DOOR) {
                    return doorPos;
                }
            }
        }
        return null;
    }

    public boolean isReplaceable(Position pos) {
        return isInsideBoard(pos)
                && getEntity(pos).getType() != EntityType.BORDER
                && getEntity(pos).getType() != EntityType.ROCK
                && getEntity(pos).getType() != EntityType.WALL;
    }

    /**
     * finds the position of the player
     * @return the position of the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * gets the position of all given componentType
     * @return a list of positions of all given componentType
     */
    public List<Position> getComponentPositions(EntityType entityType) {
        List<Position> rocksPositions = new ArrayList<>();
        for (int y = 0; y <getBoard().length; y++) {
            for (int x = 0; x < getBoard()[0].length; x++) {
                Position pos = new Position(x,y);
                Entity entity = getEntity(pos);
                if(entity.getType() == entityType) {
                    rocksPositions.add(pos);
                }
            }
        }
        return rocksPositions;
    }

    public boolean isEmpty(Position pos){
        return getEntity(pos).getType() == EntityType.VOID;
    }

    public boolean isSlippery(Position pos){
        EntityType type = getEntity(pos).getType();
        return type == EntityType.ROCK || type == EntityType.DIAMOND || type == EntityType.WALL;
    }

    public boolean isPushable(Position destination, Direction dir){
        Entity rock = getEntity(destination);
        return isInsideBoard(destination) && rock.getType() == EntityType.ROCK && isEmpty(((DeadlyEntity)rock).getPos().moveTo(dir));
    }

    @Override
    public void addObserver(Observer ob) {
        if(!observers.contains(ob)) observers.add(ob);
    }

    @Override
    public void removeObserver(Observer ob) {
        observers.remove(ob);
    }

    @Override
    public void notifyObservers(){
        for (Observer ob :
                observers) {
            ob.update();
        }
    }

}