package client.tools;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public interface ImageTools {
    static Image recolorImage(Image raw, Color color) {
        WritableImage image = new WritableImage(raw.getPixelReader(), (int) raw.getWidth(), (int) raw.getHeight());
        var writer = image.getPixelWriter();
        var reader = image.getPixelReader();

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color c = reader.getColor(x, y);
                if (c.getOpacity() > 0.01)
                    writer.setColor(x, y, color);
                else
                    writer.setColor(x, y, Color.TRANSPARENT);
            }
        }

        return image;
    }

    static Image recolorImage(String url, Color color) {
        return recolorImage(new Image(url), color);
    }
}
