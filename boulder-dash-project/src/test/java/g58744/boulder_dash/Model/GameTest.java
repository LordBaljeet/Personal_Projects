package g58744.boulder_dash.Model;

import g58744.boulder_dash.Model.Entities.DeadlyEntity;
import g58744.boulder_dash.Model.Entities.Entity;
import g58744.boulder_dash.Model.Entities.EntityType;
import g58744.boulder_dash.Model.Entities.Player;
import javafx.geometry.Pos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Model game;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.start(0);
    }

    @Test
    void start() {
        Player player = new Player(new Position(3,2));
        Position emptyPos = new Position(3,4);
        Position rockPos = new Position(3,5);
        assertEquals(player, game.getPlayer());
        assertEquals(EntityType.VOID, game.getBoard().getEntity(emptyPos).getType());
        assertEquals(EntityType.ROCK, game.getBoard().getEntity(rockPos).getType());
    }

    @Test
    void moveToDirt() {
        Player player = game.getPlayer();
        Position oldPos = player.getPos();
        Position newPos = oldPos.moveTo(Direction.N);
        Position diamondPos = newPos.moveTo(Direction.E);
        game.move(player, newPos);
        assertEquals(player.getPos(), newPos);
        assertEquals(EntityType.VOID, game.getBoard().getEntity(oldPos).getType());
        assertEquals(EntityType.CHARACTER, game.getBoard().getEntity(newPos).getType());
    }

    @Test
    void moveToDiamond() {
        Player player = game.getPlayer();
        Position oldPos = player.getPos();
        Position diamondPos = new Position(10,1);
        game.move(player, diamondPos);
        assertEquals(1, game.getCollectedDiamonds());
    }

    @Test
    void isPossibleMovePossible() {
        Position dirtPos = new Position(3,1);
        assertTrue(game.isPossibleMove(dirtPos));
    }

    @Test
    void isPossibleMoveNotPossible() {
        Position rockPos = new Position(4,2);
        assertFalse(game.isPossibleMove(rockPos));
    }

    @Test
    void getPlayer() {
        Player expected = new Player(new Position(3,2));
        assertEquals(expected, game.getPlayer());
    }

    @Test
    void hideDoor() {
        Position doorPos = game.getDoorPos();
        assertEquals(EntityType.DOOR, game.getBoard().getEntity(doorPos).getType());
        game.hideDoor();
        assertEquals(EntityType.BORDER, game.getBoard().getEntity(doorPos).getType());
    }

    @Test
    void showDoor() {
        game.hideDoor();
        Position doorPos = game.getDoorPos();
        assertEquals(EntityType.BORDER, game.getBoard().getEntity(doorPos).getType());
        game.showDoor();
        assertEquals(EntityType.DOOR, game.getBoard().getEntity(doorPos).getType());
    }

    @Test
    void gameEndedFalse() {
        assertFalse(game.GameEnded());
    }

    @Test
    void gameEndedTrue_Death() {
        Player player = game.getPlayer();
        Position SE = player.getPos().moveTo(Direction.S).moveTo(Direction.E);
        Position S = SE.moveTo(Direction.S);
        game.move(player, SE);
        game.move(player, S);
        assertTrue(game.GameEnded());
        assertTrue(player.isDead());
    }

    @Test
    void gameEndedTrue_Win() {
        Player player = game.getPlayer();
        List<Position> diamondsPos = game.getBoard().getComponentPositions(EntityType.DIAMOND);
        for (Position pos : diamondsPos) {
            game.move(player,pos);
        }
        game.move(player, game.getDoorPos());
        assertTrue(game.GameEnded());
    }

    @Test
    void abandonGame() {
        Player player = game.getPlayer();
        game.abandonGame();
        assertTrue(game.GameEnded());
    }

    @Test
    void undo() {
        Player player = game.getPlayer();
        Position oldPos = player.getPos();
        Position newPos = oldPos.moveTo(Direction.N);
        Entity prey = game.getBoard().getEntity(newPos);
        game.move(player, newPos);
        game.undo();
        assertEquals(oldPos, player.getPos());
        assertEquals(EntityType.CHARACTER, game.getBoard().getEntity(oldPos).getType());
        assertEquals(prey, game.getBoard().getEntity(newPos));
    }

    @Test
    void undoDiamond() {
        Player player = game.getPlayer();
        Position oldPos = player.getPos();
        List<Position> diamonds = game.getBoard().getComponentPositions(EntityType.DIAMOND);
        Position newPos = diamonds.get(0);
        Entity prey = game.getBoard().getEntity(newPos);
        game.move(player, newPos);
        assertEquals(1,game.getCollectedDiamonds());
        assertEquals(EntityType.CHARACTER, game.getBoard().getEntity(newPos).getType());
        game.undo();
        assertEquals(oldPos, player.getPos());
        assertEquals(prey, game.getBoard().getEntity(newPos));
        assertEquals(0, game.getCollectedDiamonds());
    }

    @Test
    void redo() {
        Player player = game.getPlayer();
        Position oldPos = player.getPos();
        Position newPos = oldPos.moveTo(Direction.N);
        Entity prey = game.getBoard().getEntity(newPos);
        game.move(player, newPos);
        game.undo();
        game.redo();
        assertEquals(newPos, player.getPos());
        assertEquals(EntityType.CHARACTER, game.getBoard().getEntity(newPos).getType());
        assertEquals(EntityType.VOID, game.getBoard().getEntity(oldPos).getType());
    }

    @Test
    void redoDiamond() {
        Player player = game.getPlayer();
        Position oldPos = player.getPos();
        List<Position> diamonds = game.getBoard().getComponentPositions(EntityType.DIAMOND);
        Position newPos = diamonds.get(0);
        Entity prey = game.getBoard().getEntity(newPos);
        game.move(player, newPos);
        assertEquals(1,game.getCollectedDiamonds());
        game.undo();
        assertEquals(0, game.getCollectedDiamonds());
        game.redo();
        assertEquals(1,game.getCollectedDiamonds());
    }

    @Test
    void gameWonFalse() {
        assertFalse(game.gameWon());
    }

    @Test
    void gameWonTrue() {
        Player player = game.getPlayer();
        List<Position> diamondsPos = game.getBoard().getComponentPositions(EntityType.DIAMOND);
        for (Position pos : diamondsPos) {
            game.move(player,pos);
        }
        game.move(player, game.getDoorPos());
        assertTrue(game.gameWon());
    }

    @Test
    void getCollectedDiamonds0() {
        assertEquals(0,game.getCollectedDiamonds());
    }

    @Test
    void getCollectedDiamonds18() {
        Player player = game.getPlayer();
        List<Position> diamondsPos = game.getBoard().getComponentPositions(EntityType.DIAMOND);
        for (Position pos : diamondsPos) {
            game.move(player,pos);
        }
        assertEquals(18,game.getCollectedDiamonds());
    }

    @Test
    void getRequiredDiamonds() {
        assertEquals(12,game.getRequiredDiamonds());
    }

    @Test
    void hasNextLevelTrue() {
        assertTrue(game.hasNextLevel());
    }

    @Test
    void hasNextLevelFalse(){
        LevelsManager mgr = new LevelsManager();
        int nb = mgr.size();
        game.start(nb-1);
        assertFalse(game.hasNextLevel());
    }

    @Test
    void manageFallingComponents() {
        Player player = game.getPlayer();
        game.move(player, player.getPos().moveTo(Direction.S));
        game.move(player, player.getPos().moveTo(Direction.E));
        DeadlyEntity rock = (DeadlyEntity) game.getBoard().getEntity(player.getPos().moveTo(Direction.N));
        Position oldRockPos = rock.getPos();
        assertEquals(EntityType.ROCK, rock.getType());
        assertFalse(rock.isFalling());
        game.move(player, player.getPos().moveTo(Direction.W));
        assertEquals(rock, game.getBoard().getEntity(oldRockPos.moveTo(Direction.S).moveTo(Direction.S)));
    }

    @Test
    void managePushingBehaviour() {
        Player player = game.getPlayer();
        Position newRockPos = player.getPos();
        game.move(player, player.getPos().moveTo(Direction.E).moveTo(Direction.E));
        Position rockPos =  player.getPos().moveTo(Direction.W);
        assertEquals(EntityType.ROCK, game.getBoard().getEntity(rockPos).getType());
        DeadlyEntity rock = (DeadlyEntity) game.getBoard().getEntity(rockPos);
        game.move(player, player.getPos().moveTo(Direction.W));
        assertEquals(rock, game.getBoard().getEntity(newRockPos));
    }
}