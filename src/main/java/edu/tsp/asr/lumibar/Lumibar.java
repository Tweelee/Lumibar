package edu.tsp.asr.lumibar;

import java.util.Calendar;
import java.util.GregorianCalendar;

import gnu.io.CommPortIdentifier;

import static spark.Spark.*;

public class Lumibar {


    public static void main(String arg[]) {


        String PORT_NAMES[] = {
                "/dev/tty.usbserial-A9007UX1", // Mac OS X
                "/dev/ttyACM0", // Raspberry Pi
                "/dev/ttyUSB0", // Linux
                "COM3", // Windows
        };

        port(8055);
        get("/", (req, res) -> "Hello");

        Contexte contexte = new Contexte();
        int heureCourante = contexte.getHeureCourante();


        // mettre à jour la couleur du plafond
        // les valeurs de RGB sont envoyées comme queryParams
        // par exemple /color/?R=255&V=255&B=255
        post("/color/", (req, res) -> {
            Couleur couleur;
            String rouge = req.queryParams("R");
            String vert = req.queryParams("V");
            String bleu = req.queryParams("B");

            if (rouge != null && bleu != null && vert != null) {
                couleur = new Couleur(Integer.parseInt(rouge), Integer.parseInt(vert), Integer.parseInt(bleu));
                return "nouvelle couleur : R : " + rouge + ", V : " + vert + ", B : " + bleu;
            } else {
                couleur = new Couleur();
                return "nouvelle couleur : blanc.";
            }

        });

       System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/tty/ACM0");

        SerialArduino serialarduino = new SerialArduino();
        serialarduino.initialize("/dev/tty/USB0");

        char id = 10;
        boolean isGradient = false;
        char red = 255;
        char green = 255;
        char blue = 26;

        System.out.println("START WRITE");
        serialarduino.write("Start demand");
        serialarduino.writeChar(id);
        serialarduino.write(String.valueOf(isGradient));
        serialarduino.writeChar(red);
        serialarduino.writeChar(green);
        serialarduino.writeChar(blue);
        serialarduino.write("End demand");
        System.out.println("END WRITE");




        // Modifier la couleur en fonction de l'heure
        // envoi direct sur le port sériel


    }
}
