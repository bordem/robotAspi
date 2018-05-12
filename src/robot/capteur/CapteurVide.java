package robot.capteur;

import robot.Direction;
import robot.Robot;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CapteurVide extends Capteur {


    public CapteurVide(Robot robot){
        super(robot);
    }


    @Override
    protected void setDetect(int x, int y, String[][] piece) {
        boolean temporaire;
        if(piece[x][y].charAt(0)=='V'){
            temporaire = detect;
            detect = temporaire;
            nPcs.firePropertyChange("Collision", temporaire, detect);
        }
        else{
            detect = false;
        }
    }




}
