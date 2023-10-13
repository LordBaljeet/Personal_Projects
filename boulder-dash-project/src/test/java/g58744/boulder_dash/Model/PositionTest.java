package g58744.boulder_dash.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void moveToNotModified() {
        Position pos = new Position(1,1);
        Position movedPos = pos.moveTo(Direction.N);
        assertEquals(1,pos.x());
        assertEquals(1,pos.y());
    }
    @Test
    void moveToSouth(){
        Position pos = new Position(1,1);
        Position expectedS = new Position(1,2);
        Position resultS = pos.moveTo(Direction.S);
        assertEquals(expectedS,resultS);
    }
    @Test
    void moveToEast(){
        Position pos = new Position(1,1);
        Position expectedE = new Position(2,1);
        Position resultE = pos.moveTo(Direction.E);
        assertEquals(expectedE,resultE);
    }
    @Test
    void moveToWest(){
        Position pos = new Position(1,1);
        Position expectedW = new Position(0,1);
        Position resultW = pos.moveTo(Direction.W);
        assertEquals(expectedW,resultW);
    }
    @Test
    void moveToNorth(){
        Position pos = new Position(1,1);
        Position expectedN = new Position(1,0);
        Position resultN = pos.moveTo(Direction.N);
        assertEquals(expectedN,resultN);
    }
    @Test
    void x() {
        Position pos = new Position(12,23);
        assertEquals(12,pos.x());
    }
    @Test
    void y() {
        Position pos = new Position(12,23);
        assertEquals(23,pos.y());
    }
}