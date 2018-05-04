package robot;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by paoli3 on 24/04/18.
 */

public class Robot implements Runnable{
    //public enum deplacement{
    //    HAUT,BAS,GAUCHE,DROITE;
    //}
    private boolean actif,rempli;
    private Batterie batterie;
    private Reserve reserve;
    private Thread thread;
    //private deplacement depla;
    //private Capteur[] capteurs = new Capteur[4];

    public Robot(Reserve reserve1, Batterie batterie1){
        actif=rempli=false;
        batterie=batterie1;
        reserve=reserve1;
        batterie.addPropertyChangeSupportListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent evt){
                actif=false;
                //fonction qui va eteindre le robot
            }
        });
        reserve.addPropertyChangeSupportListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent evt){
                rempli=true;
                //Donner l'ordre au robot de rentrer à la base
            }
        });
        thread= new Thread(this);
    }

    public Reserve getReserve(){return reserve;}
    public Batterie getBatterie(){
        return batterie;
    }

    @Override
    public void run(){
    }
}