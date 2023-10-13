package g58744.boulder_dash.Model;

import g58744.boulder_dash.Model.Commands.CommandManager;
import g58744.boulder_dash.Model.Commands.MoveCommand;
import g58744.boulder_dash.Model.Entities.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class Game implements Model{

    /*------------Attributes-----------------*/

    private int lvlIndex;
    private int collectedDiamonds;
    private int requiredDiamonds;
    private Player player;
    private Position doorPos;
    private final Board board;
    private final LevelsManager lvlMgr;
    private CommandManager cmdMgr;
    private Set<DeadlyEntity> rocks;
    private Set<DeadlyEntity> diamonds;
    public Game() {
        lvlMgr = new LevelsManager();
        board = new Board();
    }

    /*------------public Methods-----------------*/
    @Override
    public void start(int levelIndex){
        //Values Initialisation
        lvlIndex = levelIndex;
        cmdMgr = new CommandManager();
        Level currentLevel = lvlMgr.getLevel(lvlIndex);
        requiredDiamonds = currentLevel.requiredDiamonds();
        collectedDiamonds = 0;
        board.createBoard(currentLevel.levelLayout());
        player = board.getPlayer();
        doorPos = board.findDoor();

        //Lists initialisation
        rocks = new HashSet<>();
        diamonds = new HashSet<>();
        List<Position> rocksPos = board.getComponentPositions(EntityType.ROCK);
        List<Position> diamondsPos = board.getComponentPositions(EntityType.DIAMOND);
        for (Position pos : rocksPos) {rocks.add((DeadlyEntity) board.getEntity(pos));}
        for (Position pos : diamondsPos) {diamonds.add((DeadlyEntity) board.getEntity(pos));}
        manageFallingComponents();
    }
    @Override
    public void move(MovableEntity source, Position destination) {
        MoveCommand cmd = new MoveCommand(this, source, destination);
        cmdMgr.doCommand(cmd);
    }
    @Override
    public boolean isPossibleMove(Position destination){
        if(player.getPos().x() != destination.x()) {
            Direction dir = player.getPos().x() > destination.x() ? Direction.W : Direction.E;
            return board.isReplaceable(destination) || board.isPushable(destination, dir);
        }
        return board.isReplaceable(destination);
    }
    @Override
    public Player getPlayer() {
        return player;
    }
    @Override
    public void hideDoor() {
        board.replaceEntity(doorPos, new Entity(EntityType.BORDER));
    }
    @Override
    public void showDoor() {
        board.replaceEntity(doorPos,new Entity(EntityType.DOOR));
    }
    @Override
    public boolean GameEnded() {
        return player.isDead() || gameWon();
    }
    @Override
    public void abandonGame() {
        player.setDead(true);
    }
    @Override
    public void undo() {
        cmdMgr.undo();
    }
    @Override
    public void redo() {
        cmdMgr.redo();
    }
    @Override
    public boolean gameWon() {
        return collectedDiamonds >= requiredDiamonds && player.getPos().equals(doorPos);
    }
    @Override
    public int getCollectedDiamonds() {
        return collectedDiamonds;
    }
    @Override
    public int getRequiredDiamonds() {
        return requiredDiamonds;
    }
    @Override
    public Board getBoard() {
        return board;
    }
    @Override
    public boolean hasNextLevel() {
        try{
            lvlMgr.getLevel(lvlIndex +1);
        }catch(Exception e) {
            return false;
        }
        return true;
    }
    @Override
    public Position getDoorPos() {
        return doorPos;
    }
    @Override
    public List<MoveData> manageFallingComponents() {
        List<MoveData> moves = new ArrayList<>();
        do {
            setComponentTypeFallingState(EntityType.ROCK);
            setComponentTypeFallingState(EntityType.DIAMOND);
            List<DeadlyEntity> fallingRocks = getFallingComponents(EntityType.ROCK);
            List<DeadlyEntity> fallingDiamonds = getFallingComponents(EntityType.DIAMOND);
            moves.addAll(manageFallingComponent(fallingRocks));
            moves.addAll(manageFallingComponent(fallingDiamonds));

            setComponentTypeRollingState(EntityType.ROCK);
            setComponentTypeRollingState(EntityType.DIAMOND);
            List<DeadlyEntity> rollingRocks = getRollingComponents(EntityType.ROCK);
            List<DeadlyEntity> rollingDiamonds = getRollingComponents(EntityType.DIAMOND);
            moves.addAll(manageRollingComponent(rollingRocks));
            moves.addAll(manageRollingComponent(rollingDiamonds));
        }while(!getFallingComponents(EntityType.ROCK).isEmpty()
                || !getFallingComponents(EntityType.DIAMOND).isEmpty()
                || !getRollingComponents(EntityType.DIAMOND).isEmpty()
                || !getRollingComponents(EntityType.ROCK).isEmpty());
        return moves;
    }
    @Override
    public MoveData managePushingBehaviour(Position destination, Direction dir){
        MoveData data = null;
        if(board.isPushable(destination,dir)){
            DeadlyEntity rock = (DeadlyEntity) board.getEntity(destination);
            Position endPos = destination.moveTo(dir);
            data = new MoveData(destination, endPos, rock, board.getEntity(endPos));
            rock.move(endPos, board);
        }
        return data;
    }
    @Override
    public Level getCurrentLvl(){
        return lvlMgr.getLevel(lvlIndex);
    }
    @Override
    public int nbOfLevels() {
        return lvlMgr.size();
    }
    public void incrementCollectedDiamonds(){collectedDiamonds++;}
    public void decrementCollectedDiamonds(){collectedDiamonds--;}
    public void trackEntity(DeadlyEntity entity){
        if(entity.getType() != EntityType.DIAMOND && entity.getType() != EntityType.DIAMOND){
            throw new IllegalArgumentException("UnTrackable entity because its not deadly!");
        }
        if (entity.getType() == EntityType.DIAMOND) {
            diamonds.add(entity);
        } else {
            rocks.add(entity);
        }
    }
    public void unTrackEntity(DeadlyEntity entity){
        if(entity.getType() != EntityType.DIAMOND && entity.getType() != EntityType.DIAMOND){
            throw new IllegalArgumentException("UnTrackable entity because its not deadly!");
        }
        Predicate<DeadlyEntity> filter = deadlyEntity -> deadlyEntity.getPos().equals(entity.getPos());
        if (entity.getType() == EntityType.DIAMOND) {
            diamonds.removeIf(filter);
            diamonds.remove(entity);
        } else {
            rocks.removeIf(filter);
            rocks.remove(entity);
        }
    }

    /*------------Private Methods-----------------*/

    /**
     * gets the position of all currently falling componentType
     * @param entityType the componentType to get the list of
     * @throws IllegalArgumentException if the componentType is not rocks or totalDiamonds
     * @return a list of positions of all falling componentType
     */
    private List<DeadlyEntity> getFallingComponents(EntityType entityType) {
        if(entityType != EntityType.DIAMOND && entityType != EntityType.ROCK){
            throw new IllegalArgumentException("Not a component that can fall");
        }
        List<DeadlyEntity> fallingComponents = new ArrayList<>();
        Set<DeadlyEntity> components = entityType == EntityType.DIAMOND ? diamonds : rocks;
        for (DeadlyEntity comp : components) {
            if(comp.isFalling()) fallingComponents.add(comp);
        }
        return fallingComponents;
    }

    /**
     * manage the given componentsList falling mechanism
     * @throws IllegalArgumentException if the componentList is not composed of only rocks or totalDiamonds
     * @param componentList the componentList to manage
     */
    private List<MoveData> manageFallingComponent(List<DeadlyEntity> componentList) {
        List<MoveData> data = new ArrayList<>();
        for (DeadlyEntity comp : componentList) {
            Position fallingPos = comp.getPos().moveTo(Direction.S);
            data.add(new MoveData(comp.getPos(), fallingPos, comp, board.getEntity(fallingPos)));
            comp.move(fallingPos, board);
            data = fall(comp, data);
            setComponentTypeFallingState(EntityType.ROCK);
            setComponentTypeFallingState(EntityType.DIAMOND);
        }
        return data;
    }

    /**
     * sets the given componentType falling status
     * @param component the componentType to set.
     */
    private void setComponentTypeFallingState(EntityType component) {
        if(!component.equals(EntityType.DIAMOND) && !component.equals(EntityType.ROCK)) {
            throw new IllegalArgumentException("Only rocks and totalDiamonds are admissible :" + component);
        }
        Set<DeadlyEntity> components = component == EntityType.DIAMOND ? diamonds : rocks;
        for (DeadlyEntity comp : components) {
            Position southPos = comp.getPos().moveTo(Direction.S);
            if(board.isInsideBoard(southPos)) {
                comp.setAsFalling(board.isEmpty(southPos));
            }
        }
    }

    /**
     * sets the given componentType rolling status
     * @param type the componentType to set
     */
    private void setComponentTypeRollingState(EntityType type){
        if(type != EntityType.DIAMOND && type != EntityType.ROCK){
            throw new IllegalArgumentException("only rocks and diamonds are admissible!");
        }
        Set<DeadlyEntity> components = type == EntityType.DIAMOND ? diamonds : rocks;
        for (DeadlyEntity comp: components) {
            comp.setRolling(false);
            Position southPos = comp.getPos().moveTo(Direction.S);
            if(board.isInsideBoard(southPos) && board.isSlippery(southPos)){
                Position left = comp.getPos().moveTo(Direction.W);
                Position right = comp.getPos().moveTo(Direction.E);
                Position southLeft = left.moveTo(Direction.S);
                Position southRight = right.moveTo(Direction.S);

                if(board.isInsideBoard(left) && board.isEmpty(left) && board.isEmpty(southLeft)){
                    comp.setRolling(true);
                    comp.setRollingLeft(true);
                }

                else if(board.isInsideBoard(right) && board.isEmpty(right) && board.isEmpty(southRight)){
                    comp.setRolling(true);
                    comp.setRollingLeft(false);
                }
            }
        }
    }

    /**
     * gets the components currently rolling
     * @param type the component type
     * @return a list of rolling components
     * @throws IllegalArgumentException if the component type can't roll
     */
    private List<DeadlyEntity> getRollingComponents(EntityType type){
        if(type != EntityType.DIAMOND && type != EntityType.ROCK){
            throw new IllegalArgumentException("Only rocks and Diamonds are admissible!");
        }
        Set<DeadlyEntity> components = type == EntityType.DIAMOND ? diamonds : rocks;
        List<DeadlyEntity> list = new ArrayList<>();
        for (DeadlyEntity component : components) {
            if(component.isRolling()) list.add(component);
        }
        return list;
    }

    /**
     * Rolls the given list of rolling components
     * @param componentList the component list to roll
     * @return a list of moves done to accomplish rolling
     */
    private List<MoveData> manageRollingComponent(List<DeadlyEntity> componentList) {
        List<MoveData> data = new ArrayList<>();
        for (DeadlyEntity comp : componentList) {
            Position rollingPos = comp.getPos().moveTo(comp.isRollingLeft() ? Direction.W : Direction.E).moveTo(Direction.S);
            data.add(new MoveData(comp.getPos(), rollingPos, comp, board.getEntity(rollingPos)));
            comp.move(rollingPos, board);
            data = fall(comp, data);
            setComponentTypeRollingState(EntityType.ROCK);
            setComponentTypeRollingState(EntityType.DIAMOND);
        }
        return data;
    }

    /**
     * Move the falling entity downward recursively if it's possible.
     * @param entity the entity to make fall
     * @param data a list of dataMoves that will be filled.
     * @return the data list modified with new movements or the same one if no falling occurred.
     */
    private List<MoveData> fall(MovableEntity entity, List<MoveData> data){
        Position south = entity.getPos().moveTo(Direction.S);
        if(!board.isInsideBoard(south) ||(!board.isEmpty(south) && board.getEntity(south) != player)){
            return data;
        }
        MoveData moveData = new MoveData(entity.getPos(),south, entity,board.getEntity(south));
        data.add(moveData);
        if((board.getEntity(south) == player)) player.setDead(true);
        entity.move(south, board);
        return fall(entity, data);
    }

}