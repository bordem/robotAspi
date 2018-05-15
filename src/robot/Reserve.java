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
        System.out.println("J'aspire "+ aspiration +" poussiere");
        if((reserveActuelle+aspiration)>reserveMax) {
            int oldReserve = reserveActuelle;
            reserveActuelle += aspiration;
            nPcs.firePropertyChange("ReserveMaJ", oldReserve, reserveActuelle);
        }
        else{
            reserveActuelle +=aspiration;
        }
    }
    public void setReserveMax(int nouvelleReserveMax){reserveMax=nouvelleReserveMax;}

    public void viderReserve(){
        reserveActuelle=0;
    }
    public void addPropertyChangeSupportListener(PropertyChangeListener listener)
    {
        nPcs.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeSupportListener(PropertyChangeListener listener)
    {
        nPcs.removePropertyChangeListener(listener);
    }
    private boolean verification(int aspiration)
    {
        if((reserveActuelle-aspiration)>=reserveMax)
            return false;
        else
            return true;
    }

}
