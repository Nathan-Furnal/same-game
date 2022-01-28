package g55803.samegame.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nathan Furnal
 */
public class FieldTest {

    @Test
    void test_FieldCreation() {
        assertAll("Check that exceptions are thrown when the values are not in range",
                () -> {
                    // Rows
                    assertThrows(IllegalArgumentException.class, () -> new Field(1, 10, 3));
                    assertThrows(IllegalArgumentException.class, () -> new Field(20, 10, 3));
                    // Columns
                    assertThrows(IllegalArgumentException.class, () -> new Field(10, 1, 3));
                    assertThrows(IllegalArgumentException.class, () -> new Field(10, 20, 3));
                    // Colors
                    assertThrows(IllegalArgumentException.class, () -> new Field(10, 10, 1));
                    assertThrows(IllegalArgumentException.class, () -> new Field(10, 10, 6));

                });
    }

    @Test
    void test_getNRows() {
        Field f = new Field(10, 3, 3);
        assertEquals(f.getNRows(), 10);
    }

    @Test
    void test_NbRows_Exception() {
        assertThrows(IllegalArgumentException.class, () -> new Field(1, 10, 3));
    }

    @Test
    void test_NbCols_Exception() {
        assertThrows(IllegalArgumentException.class, () -> new Field(5, 1, 3));
    }

    @Test
    void test_NbColors_Exception_low() {
        assertThrows(IllegalArgumentException.class, () -> new Field(10, 10, 1));
    }

    @Test
    void test_NbColors_Exception_high() {
        assertThrows(IllegalArgumentException.class, () -> new Field(10, 10, 7));
    }

    @Test
    void test_getNCols() {
        Field f = new Field(10, 3, 3);
        assertEquals(f.getNCols(), 3);
    }

    @Test
    void test_getTile() {
        Field f = new Field(10, 3, 3);
        assertAll(() -> {
            Position p = new Position(0, 0);
            assertNotNull(f.getTile(p));
            f.setTile(p, null);
            assertNull(f.getTile(p));
            assertThrows(IllegalArgumentException.class, () -> f.getTile(new Position(11, 4))); // out of field
        });
    }

    @Test
    void test_setTile() {
        Field f = new Field(10, 10, 4);
        Position p = new Position(5, 3);
        Tile t = f.getTile(p);
        Tile old = new Tile(t.getColor());
        assertAll(() -> {
            assertEquals(t.getColor(), old.getColor());
            Color c = Color.BLUE.equals(t.getColor()) ? Color.RED : Color.BLUE;
            f.setTile(p, new Tile(c));
            assertNotEquals(f.getTile(p).getColor(), old.getColor());
            assertThrows(IllegalArgumentException.class, () -> f.setTile(new Position(10, 10), null));
        });
    }

    @Test
    void test_getScore() {
        int nRows = 5;
        int nCols = 4;
        int n = nRows * nCols;
        Field f = new Field(nRows, nCols, 3);
        f.setAllColors(Color.BLUE);
        int expected = n * (n - 1);
        assertAll("Check score before and after move",
                () -> {
                    assertEquals(0, f.getScore());
                    f.play(new Position(0, 0));
                    assertEquals(expected, f.getScore());
                });
    }

    @Test
    void test_setScore() {
        Field f = new Field(5, 5, 3);
        assertAll(() -> {
            assertEquals(f.getScore(), 0);
            f.setScore(23);
            assertEquals(f.getScore(), 23);
        });
    }

    @Test
    void test_groupColor_all() {
        int nRows = 5;
        int nCols = 7;
        Field f1 = new Field(nRows, nCols, 3);
        f1.setAllColors(Color.BLUE);
        Set<Position> s = new HashSet<>();
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                s.add(new Position(i, j));
            }
        }
        assertEquals(f1.groupColor(new Position(0, 0)), s);
    }

    @Test
    void test_groupColor_nullTile() {
        int nRows = 5;
        int nCols = 7;
        Field f1 = new Field(nRows, nCols, 3);
        f1.setAllColors(Color.BLUE);
        f1.setTile(new Position(1, 1), null);
        Set<Position> s = new HashSet<>();
        assertEquals(f1.groupColor(new Position(1, 1)), s);
    }


    /**
     * Sets the outer field as one color and the inner field as another.
     */
    @Test
    void test_groupColor_outer() {
        int nRows = 4;
        int nCols = 4;
        Field f = new Field(nRows, nCols, 2);
        Color c1 = Color.BLUE;
        Color c2 = Color.RED;
        Set<Position> expected = new HashSet<>();
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                if (i == 0 || i == nRows - 1 || j == 0 || j == nCols - 1) {
                    f.setTile(new Position(i, j), new Tile(c1));
                    expected.add(new Position(i, j));
                } else {
                    f.setTile(new Position(i, j), new Tile(c2));
                }
            }
        }
        assertAll("Outer positions should group the outer together",
                () -> {
                    assertEquals(expected, f.groupColor(new Position(0, 0)));
                    assertEquals(expected, f.groupColor(new Position(0, nCols - 1)));
                    assertEquals(expected, f.groupColor(new Position(nRows - 1, 0)));
                    assertEquals(expected, f.groupColor(new Position(nRows - 1, nCols - 1)));
                });
    }

    /**
     * Sets the outer field as one color and the inner field as another.
     */
    @Test
    void test_groupColor_inner() {
        int nRows = 4;
        int nCols = 4;
        Field f = new Field(nRows, nCols, 2);
        Color c1 = Color.BLUE;
        Color c2 = Color.RED;
        Set<Position> expected = new HashSet<>();
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                if (i == 0 || i == nRows - 1 || j == 0 || j == nCols - 1) {
                    f.setTile(new Position(i, j), new Tile(c1));
                } else {
                    f.setTile(new Position(i, j), new Tile(c2));
                    expected.add(new Position(i, j));
                }
            }
        }
        assertAll("inner positions should group the inner together",
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> f.groupColor(new Position(10, 10))); // out of field
                    assertEquals(expected, f.groupColor(new Position(1, 1)));
                    assertEquals(expected, f.groupColor(new Position(2, 2)));
                    assertEquals(expected, f.groupColor(new Position(2, 1)));
                    assertEquals(expected, f.groupColor(new Position(1, 2)));
                });
    }

    @Test
    void test_groupColor_misc() {
        // r r g b
        // r r g b
        // r r g b
        // g g g b
        // g g g b
        int nRows = 5;
        int nCols = 4;
        Field f = new Field(nRows, nCols, 3);
        Color green = Color.GREEN;
        Color red = Color.RED;
        Color blue = Color.BLUE;
        f.setAllColors(green);
        Set<Position> expectedGreen = new HashSet<>();
        Set<Position> expectedRed = new HashSet<>();
        Set<Position> expectedBlue = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                expectedRed.add(new Position(i, j));
                f.setTile(new Position(i, j), new Tile(red));
            }
        }
        for (int i = 0; i < nRows; i++) {
            for (int j = nCols - 1; j < nCols; j++) {
                expectedBlue.add(new Position(i, j));
                f.setTile(new Position(i, j), new Tile(blue));
            }
        }

        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                Position p = new Position(i, j);
                if (!(expectedRed.contains(p) || expectedBlue.contains(p))) {
                    expectedGreen.add(p);
                }
            }
        }

        assertAll("Check that each group is correct",
                () -> {
                    assertEquals(expectedGreen, f.groupColor(new Position(0, 2)));
                    assertEquals(expectedBlue, f.groupColor(new Position(nRows - 1, nCols - 1)));
                    assertEquals(expectedRed, f.groupColor(new Position(1, 1)));
                });
    }

    // Starting from this part, I'll try to put the game in "difficult" situations.

    @Test
    void test_playFallFillColumn() {
        // field 5 x 8
        // b b                                  b b
        // b b           r   ==> play green ==> b b
        // b b           g                      b b
        // b b           g                      b b
        // b b b . . r . g                      b b r r
        int nRows = 5;
        int nCols = 8;
        int nColors = 3;
        Field f = new Field(nRows, nCols, nColors);
        f.setAllColors(Color.BLUE);
        for (int i = 0; i < nRows; i++) {
            for (int j = 2; j < nCols; j++) {
                f.setTile(new Position(i, j), null);
            }
        }
        f.setTile(new Position(nRows - 1, 5), new Tile(Color.RED));
        f.setTile(new Position(1, nCols - 1), new Tile(Color.RED));
        f.setTile(new Position(2, nCols - 1), new Tile(Color.GREEN));
        f.setTile(new Position(3, nCols - 1), new Tile(Color.GREEN));
        f.setTile(new Position(4, nCols - 1), new Tile(Color.GREEN));

        Field expected = new Field(nRows, nCols, 2);
        expected.setAllColors(Color.BLUE);
        for (int i = 0; i < nRows; i++) {
            for (int j = 2; j < nCols; j++) {
                expected.setTile(new Position(i, j), null);
            }
        }
        expected.setTile(new Position(nRows - 1, 2), new Tile(Color.RED));
        expected.setTile(new Position(nRows - 1, 3), new Tile(Color.RED));

        assertAll("All tiles must be the same color or null as expected",
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> f.play(new Position(10, 10))); // out of field
                    f.play(new Position(nRows - 1, nCols - 1)); // play green
                    for (int i = 0; i < nRows; i++) {
                        for (int j = 0; j < nCols; j++) {
                            Position p = new Position(i, j);
                            assertEquals(expected.getTile(p), f.getTile(p)); // comparing either String or null
                        }
                    }
                });
    }

    @Test
    void test_playFallFillColumn_playBlue() {
        // field 5 x 8
        // b b
        // b b           r   ==> play blue  ==>    r
        // b b           g                         g
        // b b           g                         g
        // b b b . . r . g                      r  g
        int nRows = 5;
        int nCols = 8;
        int nColors = 3;
        Field f = new Field(nRows, nCols, nColors);
        f.setAllColors(Color.BLUE);
        for (int i = 0; i < nRows; i++) {
            for (int j = 2; j < nCols; j++) {
                f.setTile(new Position(i, j), null);
            }
        }
        f.setTile(new Position(nRows - 1, 5), new Tile(Color.RED));
        f.setTile(new Position(1, nCols - 1), new Tile(Color.RED));
        f.setTile(new Position(2, nCols - 1), new Tile(Color.GREEN));
        f.setTile(new Position(3, nCols - 1), new Tile(Color.GREEN));
        f.setTile(new Position(4, nCols - 1), new Tile(Color.GREEN));

        Field expected = new Field(nRows, nCols, 2);
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                expected.setTile(new Position(i, j), null);
            }
        }
        expected.setTile(new Position(nRows - 1, 0), new Tile(Color.RED));
        expected.setTile(new Position(1, 1), new Tile(Color.RED));
        expected.setTile(new Position(nRows - 1, 1), new Tile(Color.GREEN));
        expected.setTile(new Position(nRows - 2, 1), new Tile(Color.GREEN));
        expected.setTile(new Position(nRows - 3, 1), new Tile(Color.GREEN));

        assertAll("All tiles must be the same color or null as expected",
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> f.play(new Position(3, 3))); // playing null tile fails
                    f.play(new Position(1, 1)); // play blue
                    for (int i = 0; i < nRows; i++) {
                        for (int j = 0; j < nCols; j++) {
                            Position p = new Position(i, j);
                            assertEquals(expected.getTile(p), f.getTile(p)); // comparing either String or null
                        }
                    }
                });
    }

    @Test
    void test_isGameOver() {
        int nRows = 5;
        int nCols = 5;
        Field f = new Field(nRows, nCols, 3);
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                f.setTile(new Position(i, j), null);
            }
        }
        assertAll("Managing end of game situation",
                () -> {
                    assertTrue(f.isGameOver()); // all tiles are empty
                    f.setTile(new Position(nRows - 1, 0), new Tile(Color.RED)); // set a tile
                    f.play(new Position(nRows - 1, 0)); // try to play it
                    assertTrue(f.isGameOver()); // game over because one tile left can't be played
                    f.setTile(new Position(nRows - 1, 1), new Tile(Color.RED)); // Put a red tile next to the other red one
                    assertFalse(f.isGameOver()); // A last move can be played
                    f.play(new Position(nRows - 1, 0)); // Play the last move
                    assertTrue(f.isGameOver());
                });
    }

    @Test
    void test_isGameWon() {
        int nRows = 5;
        int nCols = 5;
        Field f = new Field(nRows, nCols, 3);
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                f.setTile(new Position(i, j), null);
            }
        }
        assertAll("Managing end of game situation with wins",
                () -> {
                    assertTrue(f.isGameWon()); // all tiles are empty
                    f.setTile(new Position(nRows - 1, 0), new Tile(Color.RED)); // set a tile
                    f.play(new Position(nRows - 1, 0)); // try to play it
                    assertFalse(f.isGameWon()); // game not won since a tile is left
                    f.setTile(new Position(nRows - 1, 1), new Tile(Color.RED)); // Put a red tile next to the other red one
                    assertFalse(f.isGameWon()); // A last move can be played
                    f.play(new Position(nRows - 1, 0)); // Play the last move
                    assertTrue(f.isGameWon()); // Game is done and won
                });
    }
}