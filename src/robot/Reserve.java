package robot;

import java.beans.*;
/**
 * Created by paoli3 on 24/04/18.
 */
public class Reserve {
    private PropertyChangeSupport nPcs=new PropertyChangeSupport(this);
    private int reserveMax;
    private int reserveActuelle;
    public Reserve(int reserveMax){
        this.reserveMax=reserveMax;
        this.reserveActuelle=0;
    }
    //Accesseur
    public int getReserveMax(){return  reserveMax;}
    public int getReserveActuelle(){return reserveActuelle;}
    public void setReserveActuelle(int aspiration){
        int oldReserve=reserveActuelle;
        reserveActuelle += aspiration;
        nPcs.firePropertyChange("ReserveMaJ", oldReserve, reserveActuelle);
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
