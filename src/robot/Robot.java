package robot;

import robot.capteur.Capteur;
import robot.capteur.CapteurCollision;
import robot.capteur.CapteurVide;
import robot.cartographie.Carte;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by paoli3 on 24/04/18.
 */

public class Robot implements Runnable{
    private boolean actif,rempli;
    private Batterie batterie;
    private Reserve reserve;
    private Thread thread;
    private Direction deplacement;
    private CapteurCollision collision;
    private CapteurVide vide;
    private Carte cartographie;
    private String[][] piece;
    private int posX,posY;

    public Robot(Reserve reserve1, Batterie batterie1, String[][] piece){
        this.piece=piece;
        collision = new CapteurCollision(this);
        vide = new CapteurVide(this);
        cartographie=new Carte();
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
    public Batterie getBatterie(){return batterie;}

    private void Topologie(int posY, int posX, String zone){
  /*      if(zone.charAt(0)=='0' || zone.charAt(0)=='T' ){
            cartographie.setInformation(posY,posX,false);
        }
        else{
            cartographie.setInformation(posY,posX,true);
        }*/
    }
    public Carte getCartographie(){
        return cartographie;
    }

    public String[][] getPiece() {
        return piece;
    }

    public int getX(){
        return posX;
    }

    public int getY(){
        return posY;
    }

    @Override
    public void run(){
    }


}