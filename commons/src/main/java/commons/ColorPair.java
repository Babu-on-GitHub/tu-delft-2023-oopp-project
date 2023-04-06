package commons;

import java.io.Serializable;

public class ColorPair implements Serializable {

    String background;

    String font;

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
