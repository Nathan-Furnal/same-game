package g55803.samegame.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nathan Furnal
 */
public class PositionTest {

    @Test
    void test_getRow() {
        int row = 3;
        int col = 7;
        Position p = new Position(row, col);
        assertEquals(row, p.getRow());
    }

    @Test
    void test_getCol() {
        int row = 3;
        int col = 7;
        Position p = new Position(row, col);
        assertEquals(col, p.getCol());
    }

    @Test
    void test_move() {
        int row = 1;
        int col = 2;
        Direction d = Direction.LEFT;
        Position p = new Position(row, col);
        Position expected = new Position(row, col - 1);
        Position actual = p.move(d);
        assertEquals(expected, actual);
    }

    @Test
    void testMove_AllMoveDifferent() {
        Position p = new Position(3, 5);
        assertAll("all moves reach a different point",
                () -> {
                    for (Direction d : Direction.values()) {
                        assertNotEquals(p, p.move(d));
                    }
                });
    }

    @Test
    void testEquals() {
        Position p1 = new Position(1, 2);
        Position p2 = new Position(1, 2);
        assertEquals(p1, p2);
    }

    @Test
    void testEquals_notEqual() {
        Position p1 = new Position(1, 2);
        Position p2 = new Position(1, 3);
        assertNotEquals(p1, p2);
    }
}