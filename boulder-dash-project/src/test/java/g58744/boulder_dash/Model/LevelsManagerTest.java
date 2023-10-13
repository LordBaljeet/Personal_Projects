package g58744.boulder_dash.Model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelsManagerTest {

    private static LevelsManager mgr;

    @BeforeAll
    static void beforeAll() {
        mgr = new LevelsManager();
    }

    @Test
    void getLevel0() {
        int requiredDiamonds = mgr.getLevel(0).requiredDiamonds();
        assertEquals(12,requiredDiamonds);
    }
    @Test
    void getLevel1() {
        int requiredDiamonds = mgr.getLevel(1).requiredDiamonds();
        assertEquals(10,requiredDiamonds);
    }
    @Test
    void getLevel2() {
        int requiredDiamonds = mgr.getLevel(2).requiredDiamonds();
        assertEquals(24,requiredDiamonds);
    }

    @Test
    void size() {
        assertEquals(3,mgr.size());
    }
}