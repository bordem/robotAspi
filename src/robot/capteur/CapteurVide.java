package robot.capteur;

import robot.Direction;
import robot.Robot;
import sol.Sol;
import sol.typeSol;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CapteurVide extends Capteur {


    public CapteurVide(Robot robot){
        super(robot);
    }


    @Override
    protected void setDetect(int x, int y, Sol[][] piece) {
        boolean temporaire;
        if(piece[x][y].getSol()== typeSol.VIDE){
            System.out.println("vide "+x+" "+y);
            temporaire = false;
            detect = true;
            nPcs.firePropertyChange("vide", temporaire, detect);
        }
        else{
            detect = false;
        }
    }




}
