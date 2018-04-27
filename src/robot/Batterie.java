package robot;


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
            capaciteActuelle=0;

        }
    }
    public boolean verification(int consommation){
        if((capaciteActuelle-consommation)<=0)
            return false;
        else
            return true;
    }

}
