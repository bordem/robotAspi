package robot.capteur;

import robot.Direction;
import robot.Robot;
import sol.Sol;
import sol.typeSol;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CapteurCollision extends Capteur {


    public CapteurCollision(Robot robot){
        super(robot);
    }


    @Override
    protected void setDetect(int x, int y, Sol[][] piece ){
        boolean temporaire;
        if(piece[x][y].getSol()== typeSol.OBSTACLE) {
            System.out.println("ligne "+x+"   colonne"+y);
            temporaire = false;
            detect =true;
            nPcs.firePropertyChange("Collision", temporaire, detect);
        }
        else {
            detect = false;
        }
    }




}
