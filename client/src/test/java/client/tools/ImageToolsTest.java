package client.tools;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ImageToolsTest {
    @Test
    void recolorImageTest() {
        String url = new File("src/test/java/client/tools/test.png").toURI().toString();
        assertThrows(RuntimeException.class, () ->
                ImageTools.recolorImage(url, Color.BLACK)
        );
    }
}
