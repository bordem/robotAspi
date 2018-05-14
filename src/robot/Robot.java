package robot;

import com.sun.prism.shader.Solid_ImagePattern_AlphaTest_Loader;
import robot.capteur.Capteur;
import robot.capteur.CapteurCollision;
import robot.capteur.CapteurVide;
import robot.cartographie.Carte;
import sol.Sol;
import sol.typeSol;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by paoli3 on 24/04/18.
 */

public class Robot implements Runnable{
    private boolean actif,rempli,refuser;
    private Batterie batterie;
    private Reserve reserve;
    private Thread thread;
    private Direction deplacement;
    private CapteurCollision collision;
    private CapteurVide vide;
    private Carte cartographie;
    private Sol[][] piece;
    private int posX,posY;
    private Direction orientation;

    public Robot(Reserve reserve1, Batterie batterie1, Sol[][] piece){
        this.piece=piece;
        collision = new CapteurCollision(this);
        vide = new CapteurVide(this);
        cartographie=new Carte();
        actif=rempli=false;
        batterie=batterie1;
        reserve=reserve1;
        refuser=false;
        batterie.addPropertyChangeSupportListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent evt){
                actif=false;
                System.out.println("Le robot n'a plus de batterie et va s'eteindre");
                //fonction qui va eteindre le robot
            }
        });
        reserve.addPropertyChangeSupportListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent evt){
                rempli=true;
                System.out.println("Le robot à la réserve de poussière rempli,il va donc arrêter d'aspirer");
                //Donner l'ordre au robot de rentrer à la base
            }
        });

        collision.addPropertyChangeSupportListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                refuser=true;
                System.out.println("Collision");
            }
        });

        vide.addPropertyChangeSupportListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                refuser=true;
                System.out.println("Espace vide");
            }
        });

        thread= new Thread(this);
    }

    public Reserve getReserve(){return reserve;}
    public Batterie getBatterie(){return batterie;}


    public void DeplacerRobot(Direction direction){
        Sol memoire = piece[posX][posY];

        collision.detecteur(direction);
        vide.detecteur(direction);

        if(!refuser) {
            if (actif) {
                switch (direction) {
                    case BAS:
                        posY--;
                        break;
                    case HAUT:
                        posY++;
                        break;
                    case DROITE:
                        posX++;
                        break;
                    case GAUCHE:
                        posX--;
                        break;
                }
                if (!rempli) {
                    aspirer(memoire.getEpaisseurPoussiere());
                }
                if (memoire.getSol()== typeSol.TAPIS) {
                    if (orientation == direction) {
                        batterie.consommation_obstacle();
                    } else
                        batterie.consommation_virage_sol();
                } else if (memoire.getSol()==typeSol.NORMAL) {
                    if (orientation == direction) {
                        batterie.consommation_normale();
                    } else {
                        batterie.consommation_virage();
                    }
                }
                orientation = direction;
                cartographie.setInformation(posX, posY, false);
            }
        }
        else{
            switch (direction) {
                case BAS:
                    posY--;
                    break;
                case HAUT:
                    posY++;
                    break;
                case DROITE:
                    posX++;
                    break;
                case GAUCHE:
                    posX--;
                    break;
            }
            cartographie.setInformation(posX, posY, true);
            refuser = true;
        }
    }

    private void aspirer(int aspiration){
        reserve.setReserveActuelle(aspiration);
    }






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

    public Sol[][] getPiece() {
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