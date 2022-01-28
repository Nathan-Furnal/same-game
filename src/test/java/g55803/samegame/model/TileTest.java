package g55803.samegame.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nathan Furnal
 */
class TileTest {

    @Test
    void test_getColor() {
        assertAll("Check same colors are equal",
                () -> {
                    assertEquals(new Tile(Color.RED).getColor(), Color.RED);
                    assertEquals(new Tile(Color.BLUE).getColor(), new Tile(Color.BLUE).getColor());
                    assertNotEquals(new Tile(Color.RED).getColor(), new Tile(Color.GREEN).getColor());
                    assertEquals(new Tile(Color.PURPLE), new Tile(Color.PURPLE)); // equality without getColor
                });
    }
}