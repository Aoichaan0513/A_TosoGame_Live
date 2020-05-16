package jp.aoichaan0513.A_TosoGame_Live.API.Maps;

public class RgbColor {

    private int r;
    private int g;
    private int b;

    public RgbColor(int red, int green, int blue) {
        r = red;
        g = green;
        b = blue;
    }

    public void setRed(int value) {
        r = value;
    }

    public void setGreen(int value) {
        g = value;
    }

    public void setBlue(int value) {
        b = value;
    }

    public int getRed() {
        return r;
    }

    public int getGreen() {
        return g;
    }

    public int getBlue() {
        return b;
    }

}
