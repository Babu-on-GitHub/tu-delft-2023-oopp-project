package commons;

import java.io.Serializable;
import java.util.Objects;

public class ColorPair implements Serializable {

    String background = "#FFFFFF";

    String font = "#000000";

    public ColorPair() {
    }

    public ColorPair(String background, String font) {
        this.background = background;
        this.font = font;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColorPair colorPair = (ColorPair) o;

        if (!Objects.equals(background, colorPair.background)) return false;
        return Objects.equals(font, colorPair.font);
    }

    @Override
    public int hashCode() {
        int result = background != null ? background.hashCode() : 0;
        result = 31 * result + (font != null ? font.hashCode() : 0);
        return result;
    }
}
