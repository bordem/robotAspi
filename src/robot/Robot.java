package robot;


import robot.capteur.CapteurBase;
import robot.capteur.CapteurCollision;
import robot.capteur.CapteurVide;
import robot.cartographie.Carte;
import robot.exception.BatterieException;
import robot.exception.ReserveException;
import sol.Sol;
import sol.typeSol;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * Created by paoli3 on 24/04/18.
 */

public class Robot implements Runnable{
    private boolean actif,rempli,refuser,nb_base ;
    private Batterie batterie;
    private Reserve reserve;
    private Thread thread;
    private Direction deplacement;
    private CapteurBase base;
    private CapteurCollision collision;
    private CapteurVide vide;
    private Carte cartographie;
    private Sol[][] piece;
    private int posX,posY, nb_deplacement=0;
    private Direction orientation;
    private ArrayList<Direction> toutDeplacement;



    public Robot(Reserve reserve1, Batterie batterie1, Sol[][] piece){
        toutDeplacement= new ArrayList<Direction>();
        this.piece=new Sol[piece.length][piece[0].length];
        base = new CapteurBase(this);
        collision = new CapteurCollision(this);
        vide = new CapteurVide(this);
        cartographie=new Carte();
        actif=true;
        batterie=batterie1;
        reserve=reserve1;
        refuser=false;
        rempli=false;
        for(int i=0; i < this.piece.length;i++ )
        {
            for(int j=0; j< this.piece[i].length; j++){
                this.piece[i][j]=piece[i][j];
                if(this.piece[i][j].getSol()==typeSol.BASE)
                {
                    posX=j;
                    posY=i;
                }
                this.piece[i][j].afficherSol();
            }
            System.out.println();
        }
        System.out.println("X "+posX+" Y "+posY);
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
                System.out.println("Le robot à la réserve de poussière rempli,il va donc arrêter d'aspirer");
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

        base.addPropertyChangeSupportListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                batterie.rechargerBatterie();
                reserve.viderReserve();
                actif=true;
            }
        });

        thread= new Thread(this);
    }

    public Reserve getReserve(){return reserve;}
    public Batterie getBatterie(){return batterie;}


    public void deplacerRobot(Direction direction) throws BatterieException{
        Sol memoire = piece[posY][posX];
        collision.detecteur(direction);
        vide.detecteur(direction);
        if(!refuser) {
            if (actif) {
                switch (direction) {
                    case BAS:
                        posY++;
                        System.out.println("Bas "+posY);
                        break;
                    case HAUT:
                        posY--;
                        System.out.println("Haut "+posY);
                        break;
                    case DROITE:
                        posX++;
                        System.out.println("Droite "+posX);
                        break;
                    case GAUCHE:
                        posX--;
                        System.out.println("gauche "+posX);
                        break;
                }

                if (!rempli) {
                    aspirer( piece[posY][posX].getEpaisseurPoussiere());
                    piece[posY][posX].setEpaisseurPoussiere(memoire.getEpaisseurPoussiere());
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
                toutDeplacement.add(direction);
                orientation = direction;
                cartographie.setInformation(posX, posY, false);
                nb_deplacement++;
            }else{
                throw new BatterieException();
            }
        }
        else{
                switch (direction) {
                    case BAS:
                        cartographie.setInformation(posX, posY+1, true);
                        break;
                    case HAUT:
                        cartographie.setInformation(posX, posY-1, true);
                        break;
                    case DROITE:
                        cartographie.setInformation(posX+1, posY, true);
                        break;
                    case GAUCHE:
                        cartographie.setInformation(posX-1, posY, true);
                        break;
                }
                refuser = false;
        }
    }

    private void aspirer(int aspiration){
        reserve.setReserveActuelle(aspiration);
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

    public int getNb_deplacement(){
        return nb_deplacement;
    }
    public int getY(){
        return posY;
    }

    private void retournerBase(){
        for(int i=toutDeplacement.size()-1; i>=0; i--){
            Direction direction=null;
            Sol memoire = piece[posY][posX];
            switch (toutDeplacement.remove(i)){
                case GAUCHE: posX++;
                direction=Direction.DROITE;
                    break;
                case DROITE: posX--;
                direction=Direction.GAUCHE;
                    break;
                case HAUT:  posY++;
                 direction=Direction.BAS;
                    break;
                case BAS: posY--;
                direction=Direction.HAUT;
                    break;
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
            try {
                verificateActif();
                Thread.sleep(50);
            }catch (InterruptedException ie){
                ie.printStackTrace();
            }
            catch (BatterieException be) {
                be.printStackTrace();
            }
        }
        System.out.println("pos x "+posX+" pos y"+posY);
    }





    private void automateDeplacement(Direction direction){
            try {
                if(nb_deplacement==0) {
                    switch (direction) {
                        case BAS:
                            posY++;
                            System.out.println("Bas " + posY);
                            break;
                        case HAUT:
                            posY--;
                            System.out.println("Haut " + posY);
                            break;
                        case DROITE:
                            posX++;
                            System.out.println("Droite " + posX);
                            break;
                        case GAUCHE:
                            posX--;
                            System.out.println("gauche " + posX);
                            break;
                    }
                    if(!rempli){
                        aspirer(piece[posY][posX].getEpaisseurPoussiere());
                        piece[posY][posX].setEpaisseurPoussiere(piece[posY][posX].getEpaisseurPoussiere());
                    }
                    if (piece[posY][posX].getSol()== typeSol.TAPIS) {
                        if (orientation == direction) {
                            batterie.consommation_obstacle();
                        } else
                            batterie.consommation_virage_sol();
                    } else if (piece[posY][posX].getSol()==typeSol.NORMAL) {
                        if (orientation == direction) {
                            batterie.consommation_normale();
                        } else {
                            batterie.consommation_virage();
                        }
                    }
                    toutDeplacement.add(direction);
                    orientation = direction;
                    cartographie.setInformation(posX, posY, false);
                    nb_deplacement++;
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

    }

    private Direction choixAutomate(){
        collision.detecteur(Direction.HAUT);
        vide.detecteur(Direction.HAUT);

        if(refuser){
            refuser=true;
            cartographie.setInformation(posX, posY--, false);
            collision.detecteur(Direction.BAS);
            vide.detecteur(Direction.BAS);

            if(refuser){
                refuser=true;
                collision.detecteur(Direction.DROITE);
                vide.detecteur(Direction.DROITE);
                cartographie.setInformation(posX, posY++, false);

                if(refuser){
                    refuser=true;
                    collision.detecteur(Direction.GAUCHE);
                    vide.detecteur(Direction.GAUCHE);
                    cartographie.setInformation(posX--, posY, false);

                    if(refuser){
                        cartographie.setInformation(posX++, posY, false);
                        throw new IllegalStateException("Le robot est bloqué");
                    }
                    else{
                        return Direction.GAUCHE;
                    }
                }
                else{
                    return Direction.DROITE;
                }
            }
            else
                return Direction.BAS;
        }
        else
            return Direction.HAUT;
    }


    @Override
    public void run(){
        while(true){
            try{
                if(nb_deplacement==0) {
                    verificateActif();
                    automateDeplacement(choixAutomate());
                }
                else{
                    verificateActif();
                    verificateSac();

                }
            }
            catch (BatterieException be) {
                be.printStackTrace();
            }
            catch (ReserveException re){
                re.printStackTrace();
                retournerBase();
            }
        }

    }



    public boolean verificateActif() throws BatterieException{
        if(actif){
            return actif;
        }else {
            throw new BatterieException();
        }
    }

    public boolean verificateSac() throws ReserveException{
        if(rempli){
            return rempli;
        }else {
            throw new ReserveException();
        }
    }


    public Thread getThread(){
        return thread;
    }


}