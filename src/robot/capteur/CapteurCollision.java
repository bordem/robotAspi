package robot.capteur;

import robot.Direction;
import robot.Robot;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CapteurCollision extends Capteur {


    public CapteurCollision(Robot robot){
        super(robot);
    }


    @Override
    protected void setDetect(int x, int y, String[][] piece ){
        boolean temporaire;
        if(piece[x][y].charAt(0)=='O') {
            temporaire = detect;

            nPcs.firePropertyChange("Collision", temporaire, detect);
        }
        else {
            detect = false;
        }


    }




}
