package edu.tsp.asr.lumibar;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by mps on 08/01/16.
 * <p>
 * Contexte local du bar : heure.
 */
public class Contexte {

    private Calendar calendrier;
    private int heureCourante;
    //private boolean incendie;

    public Contexte() {

        this.calendrier = new GregorianCalendar();
        this.heureCourante = calendrier.get(Calendar.HOUR_OF_DAY);

        //  this.incendie = false;

    }

    public int getHeureCourante() {
        return this.heureCourante;
    }

    /*public boolean getIcendie(){
        return this.incendie;
    }
*/

}
