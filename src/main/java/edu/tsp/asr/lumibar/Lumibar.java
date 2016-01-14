package edu.tsp.asr.lumibar;

import twitter4j.Query;

import java.util.Scanner;

import static spark.Spark.*;


public class Lumibar {
    public static void main(String arg[]) throws InterruptedException {
        SerialCommJssc serial = new SerialCommJssc();
        ColorManager colorManager = new ColorManager(serial, 16);

        Scanner sc;
        String word;
        sc = new Scanner(System.in);
        System.out.println("Enter the word you want to count in the tweets during the match mode.");
        word = sc.nextLine();

        // debug ports
        colorManager.getSerial().listPorts();

        // pour que le destinataire comprenne le sens des données recues
        // on les envoie entre les messages start et end
        // elles sont ensuite envoyées dans le même ordre
        // id du groupe de diode (0-255)
        // nombre d'étapes dans le dégradé à appliquer pour aller à la couleur (0-255)
        // valeur R (0-255)
        // valeur G (0-255)
        // valeur B (0-255)


        port(8055);
        get("/", (req, res) -> "Hello");


        // mettre à jour la couleur du plafond
        // les valeurs de RGB sont envoyées comme paramètres POST
        post("/color/", (req, res) -> {
            String stripId = req.queryParams("id");
            String gradient = req.queryParams("gradient");
            String red = req.queryParams("R");
            String green = req.queryParams("V");
            String blue = req.queryParams("B");

            if (red != null && blue != null && green != null) {
                Color color;
                color = new Color(red, green, blue);

                colorManager.sendColor(stripId, gradient, color);

                return "id du groupe de diode: " + stripId +
                        "nouvelle color : R : " + red +
                        ", V : " + green +
                        ", B : " + blue +
                        "étapes dans le dégradé : " + gradient;
            } else {
                return "nouvelle couleur : blanc.";
            }

        });


        // Mode Match
        // récupérer l'ambiance du match depuis les tweets
        // envoi des couleurs sur le port sériel


        // choisir l'équipe à suivre
        post("/team/", (req, res) -> {
            System.out.println("Start");
            String id = req.queryParams("id");
            String team = req.queryParams("team");
            String mode = req.queryParams("mode");
            if (team != null) {
                System.out.println(mode);
                int nb;
                Query query = new Query("@"+team);
                query.setCount(50);
                TweetEngine tweetEngine = new TweetEngine();

                // ATTENTION
                // Search is rate limited at 180 queries per 15 minute window.

                while (mode.compareToIgnoreCase("true")==0) {

                    nb = tweetEngine.searchTweets(query, word);
                    System.out.println("num of tweets :" + nb);

                    // TODO :
                    // coefficients de color à calibrer
                    Color color = new Color(nb * 64, nb * 64, nb * 64);

                    colorManager.sendColorToAll(color);

                    Thread.sleep(300000);

                }
                System.out.println("End of match");

                return "ok";/*"Nombre d'occurences du mot but : " +nb +
                            "id du groupe de diode: "+ id+
                            "nouvelle couleur : R : " + nb*5 +
                            ", V : " + nb*5 +
                            ", B : " + nb*5 +
                            "étapes dans le dégradé : " + 0;*/
            } else {
                return "erreur.";
            }
        });


        // TODO : changer la calibration des couleurs (et éventuellement les plages horaires)
        // Modifier la couleur en fonction de l'heure
        // envoi direct sur le port sériel

        post("/time/", (req, res) -> {
            String mode = req.queryParams("mode");
            Contexte contexte = new Contexte();
            while ( mode.compareToIgnoreCase("true") == 0 ){
                if(contexte.getHeureCourante() < 11 && contexte.getHeureCourante() > 6) {
                    colorManager.sendColorToAll(new Color(255,255,0), 255);
                }
                else if ( contexte.getHeureCourante() > 11 && contexte.getHeureCourante() < 15 ){
                    colorManager.sendColorToAll(new Color(255,255,255), 255);
                }
                else if ( contexte.getHeureCourante() > 15 && contexte.getHeureCourante() < 19 ){
                    colorManager.sendColorToAll(new Color(255,255,0), 255);
                }
                else {
                    colorManager.sendColorToAll(new Color(255,0,0), 255);
                }

                Thread.sleep(1800000);
            }

            return "ok";
        });


        //  changer la couleur de la lumière en fonction du niveau sonore
        // TODO : calibrer les changements de couleurs.


        post("/sound/", (req, res) -> {
                    String id = req.queryParams("id");
                    String gradient = req.queryParams("gradient");
                    String mode = req.queryParams("mode");
                    while (mode.compareToIgnoreCase("true") == 0) {
                        String result = serial.readData(3);
                        int coeff = Integer.parseInt(result)/2;
                        if(coeff > 255) {
                            coeff = 255;
                        }
                        // Debug
                        System.out.println("Res : " + result + " - coeff : " + coeff);
                        Color c = new Color(coeff, coeff, coeff);
                        colorManager.sendColor(0,0,c);
                    }

                    return "ok";
                }
        );

    }



}
