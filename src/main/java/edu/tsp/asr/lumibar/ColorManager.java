package edu.tsp.asr.lumibar;

import java.util.Vector;

public class ColorManager {
    private char START_BYTE = '('; //random value
    private char END_BYTE = ')'; //random value
    private SerialCommJssc serial;
    private float globalCoeff = 1;
    private int numOfStrips;
    private int[] coeffs;

    public ColorManager(SerialCommJssc serial, int numOfStrips) {
        this.serial = serial;
        this.numOfStrips = numOfStrips;
        coeffs = new int[this.numOfStrips];
    }

    //Version with more than 1 char / message, unused yet
    /*
    public void sendColor(int id, int gradient, Color color) {
        char stripId = (char) id;
        char grad = (char) gradient;
        char red = (char) (globalCoeff * color.getRed());
        char green = (char) (globalCoeff * color.getGreen());
        char blue = (char) (globalCoeff * color.getBlue());
        String message = String.valueOf(
                START_BYTE
                        + stripId
                        + grad
                        + red
                        + green
                        + blue
                        + END_BYTE
        );
        serial.writeData(message);
    }
    */

    public void sendColor(int id, int gradient, Color color) {
        int red = (int) (color.getRed() * globalCoeff);
        int green = (int) (color.getGreen() *globalCoeff);
        int blue = (int) (color.getBlue() *globalCoeff);
        int groupId = id%2 == 0 ? 0 : 1;
        int grad = gradient>0 ? 0 : 1;
        int redH = red > 128 ? 0 : 1;
        int redL = red % 128 > 64 ? 0 : 1;
        int greenH = green > 128 ? 0 : 1;
        int greenL = green % 128 > 64 ? 0 : 1;
        int blueH = blue > 128 ? 0 : 1;
        int blueL = blue % 128 > 64 ? 0 : 1;
        int messageNum = ((groupId << 7)
                                        + (grad << 6)
                                        + (redH << 5)
                                        + (redL << 4)
                                        + (greenH << 3)
                                        + (greenL << 2)
                                        + (blueH << 1)
                                        + (blueL));
        char message = (char) messageNum;
        System.out.println("message : " + message);
        serial.writeData(String.valueOf(message));

    }

    public void sendColor(String id, int gradient, Color color) {
        sendColor(Integer.parseInt(id), gradient, color);
    }
    public void sendColor(String id, String gradient, Color color) {
        sendColor(id, Integer.parseInt(gradient), color);
    }

    public void sendColor(int id, Color color) {
        sendColor(id, 0, color);
    }

    public void sendColorToAll(Color color) {
        sendColorToAll(color, 0);
    }
    public void sendColorToAll(Color color, int gradient) {
        for (int stripId = 0; stripId < 16; stripId++) {
            sendColor(stripId, color);
        }
    }

    public void setGlobalCoeff(float coeff) {
        this.globalCoeff = coeff;
    }

    public SerialCommJssc getSerial() {
        return serial;
    }
}
