package client.tools;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ColorToolsTest {

    @Test
    void toHexStringTest() {
        assertEquals("#000000", ColorTools.toHexString(Color.BLACK));
        assertEquals("#ffffff", ColorTools.toHexString(Color.WHITE));
        assertEquals("#ff0000", ColorTools.toHexString(Color.RED));
        assertEquals("#0000ff", ColorTools.toHexString(Color.BLUE));
        assertEquals("#ffff00", ColorTools.toHexString(Color.YELLOW));
        assertEquals("#00ffff", ColorTools.toHexString(Color.CYAN));
    }

    @Test
    void makeColorStringTest() {
        assertEquals("0x000000FF", ColorTools.makeColorString(Color.BLACK));
        assertEquals("0xFFFFFFFF", ColorTools.makeColorString(Color.WHITE));
        assertEquals("0xFF0000FF", ColorTools.makeColorString(Color.RED));
        assertEquals("0x0000FFFF", ColorTools.makeColorString(Color.BLUE));
        assertEquals("0xFFFF00FF", ColorTools.makeColorString(Color.YELLOW));
        assertEquals("0x00FFFFFF", ColorTools.makeColorString(Color.CYAN));
    }
}
