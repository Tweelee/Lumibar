package edu.tsp.asr.lumibar;

import jssc.SerialPortList;

import static spark.Spark.*;


public class Lumibar {


    public static void main(String arg[]) throws InterruptedException {


        int i = 0;

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
                        Boolean.valueOf(gradient),
                        Integer.parseInt(rouge),
                        Integer.parseInt(vert),
                        Integer.parseInt(bleu));

                serial.writeData("START");
                serial.writeData(""+couleur.getId());
                serial.writeData(""+couleur.isGradient());
                serial.writeData(""+couleur.getRouge());
                serial.writeData(""+couleur.getVert());
                serial.writeData(""+couleur.getBleu());
                serial.writeData("END");


                return "id du groupe de diode: "+ id+
                        "nouvelle couleur : R : " + rouge +
                        ", V : " + vert +
                        ", B : " + bleu +
                        "en dégradé : " + gradient;
            } else {
               return "nouvelle couleur : blanc.";
            }

        });




        // TODO
        // Modifier la couleur en fonction de l'heure
        // envoi direct sur le port sériel


    }
}
