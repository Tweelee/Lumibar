package edu.tsp.asr.lumibar;

import jssc.SerialPortList;
import twitter4j.Query;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Scanner;

import static spark.Spark.*;


public class Lumibar {


    public static void main(String arg[]) throws InterruptedException {

        char START_BYTE = 47;
        char END_BYTE = 49;
        int i = 0;

        Scanner sc;
        String word;
        sc = new Scanner(System.in);
        System.out.println("Enter the word you want to count in the tweets during the match mode.");
        word = sc.nextLine();
        SerialCommJssc serial = new SerialCommJssc();

        // debug ports
        serial.listPorts();

        // pour que le destinataire comprenne le sens des données recues
        // on les envoie entre les messages start et end
        // elles sont ensuite envoyées dans le même ordre
        // id du groupe de diode
        // boolean : dégradé ou couleur fixe
        // valeur R
        // valeur V
        // valeur B


        port(8055);
        get("/", (req, res) -> "Hello");

        Contexte contexte = new Contexte();
        int heureCourante = contexte.getHeureCourante();


        // mettre à jour la couleur du plafond
        // les valeurs de RGB sont envoyées comme queryParams
        // par exemple /color/?R=255&V=255&B=255
        post("/color/", (req, res) -> {
            String id = req.queryParams("id");
            String gradient = req.queryParams("gradient");
            String rouge = req.queryParams("R");
            String vert = req.queryParams("V");
            String bleu = req.queryParams("B");

            if (rouge != null && bleu != null && vert != null) {
                Couleur couleur;
                couleur = new Couleur(Integer.parseInt(id),
                        Integer.parseInt(gradient),
                        Integer.parseInt(rouge),
                        Integer.parseInt(vert),
                        Integer.parseInt(bleu));

                String message = String.valueOf(START_BYTE) +
                        (char) couleur.getId() +
                        (char) couleur.getGradient() +
                        (char) couleur.getRouge() +
                        (char) couleur.getVert() +
                        (char) couleur.getBleu()
                        + END_BYTE + END_BYTE;

                serial.writeData(message);

                return "id du groupe de diode: " + id +
                        "nouvelle couleur : R : " + rouge +
                        ", V : " + vert +
                        ", B : " + bleu +
                        "étapes dans le dégradé : " + gradient;
            } else {
                return "nouvelle couleur : blanc.";
            }

        });


        // récupérer l'ambiance du match depuis les tweets
        // envoi des couleurs sur le port sériel


        // choisir l'équipe à suivre
        post("/team/", (req, res) -> {
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

                    // TODO
                    // coefficients de couleur à calibrer
                    Couleur couleur = new Couleur(Integer.parseInt(id), 0, nb * 5, nb * 5, nb * 5);

                    String message = String.valueOf(START_BYTE) +
                            (char) couleur.getId() +
                            (char) couleur.getGradient() +
                            (char) couleur.getRouge() +
                            (char) couleur.getVert() +
                            (char) couleur.getBleu()
                            + END_BYTE + END_BYTE;

                    serial.writeData(message);
                    Thread.sleep(300000);

                }

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


        // TODO
        // Modifier la couleur en fonction de l'heure
        // envoi direct sur le port sériel


    }
}
