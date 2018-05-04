package robot;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Created by paoli3 on 24/04/18.
 */
public class Batterie {

    private PropertyChangeSupport nPcs=new PropertyChangeSupport(this);
    private int capaciteMax;
    private int capaciteActuelle;

    public Batterie(int capacite){
        capaciteMax=capaciteActuelle=capacite;
    }

    public int getCapaciteActuelle() {
        return capaciteActuelle;
    }
    public int getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteActuelle(int consommation) {
        if(verification(consommation))
            this.capaciteActuelle -= consommation;
        else{
            int oldCapa = capaciteActuelle;
            capaciteActuelle=0;
            nPcs.firePropertyChange("ArretRobot",oldCapa, capaciteActuelle);
        }
    }

    public boolean verification(int consommation){
        if((capaciteActuelle-consommation)<=0)
            return false;
        else
            return true;
    }

    public void addPropertyChangeSupportListener(PropertyChangeListener listener)
    {
        nPcs.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeSupportListener(PropertyChangeListener listener)
    {
        nPcs.removePropertyChangeListener(listener);
    }

}