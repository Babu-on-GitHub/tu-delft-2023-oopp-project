package client.tools;

import javafx.scene.paint.Color;

public interface ColorTools {
    private static String format(double val) {
        String in = Integer.toHexString((int) Math.round(val * 255));
        return in.length() == 1 ? "0" + in : in;
    }

    static String toHexString(Color value) {
        return "#" + (format(value.getRed()) + format(value.getGreen()) + format(value.getBlue()));
    }

    static String makeColorString(Color color) {
        int r = ((int) Math.round(color.getRed()     * 255)) << 24;
        int g = ((int) Math.round(color.getGreen()   * 255)) << 16;
        int b = ((int) Math.round(color.getBlue()    * 255)) << 8;
        int a = ((int) Math.round(color.getOpacity() * 255));
        return String.format("0x%08X", (r + g + b + a));
    }
}

