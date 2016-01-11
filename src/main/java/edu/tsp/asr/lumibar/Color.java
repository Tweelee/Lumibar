package edu.tsp.asr.lumibar;

public class Color {
    private int red;
    private int green;
    private int blue;

    public Color() {
        this.red = 255;
        this.green = 255;
        this.blue = 255;
    }

    public Color(int red, int green, int blue){
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color(String red, String green, String blue){
        this.red = Integer.parseInt(red);
        this.green = Integer.parseInt(green);
        this.blue = Integer.parseInt(blue);
    }

    public void setRed(int red) {
        this.red = red;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }
}
