package robot.capteur;

import robot.Direction;
import robot.Robot;
import sol.Sol;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class Capteur {
    protected PropertyChangeSupport nPcs=new PropertyChangeSupport(this);
    protected boolean detect;
    private Robot robot;
    public Capteur(Robot robot){
        detect = false;
        this.robot = robot;
    }

    public void detecteur( Direction direction){
        switch(direction)
        {
            case BAS:  setDetect( robot.getY()+1, robot.getX(), robot.getPiece());
                break;
            case HAUT: setDetect( robot.getY()-1, robot.getX(), robot.getPiece());
                break;
            case DROITE: setDetect( robot.getY(),robot.getX()+1, robot.getPiece());
                break;
            case GAUCHE: setDetect( robot.getY(),robot.getX()-1, robot.getPiece());
                break;
        }
    }


    protected abstract void setDetect(int x, int y, Sol[][] piece);
    public void addPropertyChangeSupportListener(PropertyChangeListener listener)
    {
        nPcs.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeSupportListener(PropertyChangeListener listener) {
        nPcs.removePropertyChangeListener(listener);
    }

}
