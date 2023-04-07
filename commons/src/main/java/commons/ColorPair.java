package commons;

import java.io.Serializable;
import java.util.Objects;

public class ColorPair implements Serializable, Cloneable {

    private String background = "#ffffff";

    private String font = "#111111";

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
        return Objects.equals(background, colorPair.background)
                && Objects.equals(font, colorPair.font);
    }

    @Override
    public int hashCode() {
        return Objects.hash(background, font);
    }

    @Override
    public ColorPair clone() throws CloneNotSupportedException {
        ColorPair colorPair = (ColorPair) super.clone();
        colorPair.setBackground(background);
        colorPair.setFont(font);
        return colorPair;
    }
}
