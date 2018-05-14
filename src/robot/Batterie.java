package robot;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Created by paoli3 on 24/04/18.
 */
public class Batterie {

    private PropertyChangeSupport nPcs=new PropertyChangeSupport(this);
    private final double capaciteMax;
    private double capaciteActuelle;

    public Batterie(){
        capaciteMax=100;
        capaciteActuelle=100;
    }

    public double getCapaciteActuelle() {
        return capaciteActuelle;
    }
    public double getCapaciteMax() {return capaciteMax;}

    public void rechargerBatterie(){
        capaciteActuelle=capaciteMax;
    }

    public void consommation_normale(){
        if(verification(1))
            this.capaciteActuelle -= 1;
        else{
            double oldCapa = capaciteActuelle;
            capaciteActuelle=0;
            nPcs.firePropertyChange("ArretRobot",oldCapa, capaciteActuelle);
        }
    }
    public void consommation_obstacle(){
        if(verification(1.5))
            this.capaciteActuelle -= 1.5;
        else{
            double oldCapa = capaciteActuelle;
            capaciteActuelle=0;
            nPcs.firePropertyChange("ArretRobot",oldCapa, capaciteActuelle);
        }
    }
    public void consommation_virage(){
        if(verification(2.0))
            this.capaciteActuelle -= 2.0;
        else{
            double oldCapa = capaciteActuelle;
            capaciteActuelle=0;
            nPcs.firePropertyChange("ArretRobot",oldCapa, capaciteActuelle);
        }
    }



    private boolean verification(double consommation){
        if((capaciteActuelle-consommation)<=0)
            return false;
        else
            return true;
    }

    public void addPropertyChangeSupportListener(PropertyChangeListener listener)
    {
        nPcs.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeSupportListener(PropertyChangeListener listener) {
        nPcs.removePropertyChangeListener(listener);
    }

}