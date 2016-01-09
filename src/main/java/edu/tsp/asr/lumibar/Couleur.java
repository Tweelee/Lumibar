package edu.tsp.asr.lumibar;

/**
 * Created by mps on 08/01/16.
 */
public class Couleur {

    private int rouge;
    private int vert;
    private int bleu;

    public Couleur (){
        this.rouge = 255;
        this.vert = 255;
        this.bleu = 255;
    }

    public Couleur(int rouge, int vert, int bleu){
        this.rouge = rouge;
        this.vert = vert;
        this.bleu = bleu;
    }

    public void setRouge(int rouge) {
        this.rouge = rouge;
    }

    public void setVert(int vert) {
        this.vert = vert;
    }

    public void setBleu(int bleu) {
        this.bleu = bleu;
    }

    public int getRouge() {
        return rouge;
    }

    public int getVert() {
        return vert;
    }

    public int getBleu() {
        return bleu;
    }
}
