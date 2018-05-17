package robot;


import robot.capteur.CapteurBase;
import robot.capteur.CapteurCollision;
import robot.capteur.CapteurVide;
import robot.cartographie.Carte;
import robot.exception.BatterieException;
import robot.exception.ParcoursException;
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
    private boolean actif,rempli,refuser;
    private Batterie batterie;
    private Reserve reserve;
    private Thread thread;
    private Direction deplacement;
    private CapteurBase base;
    private CapteurCollision collision;
    private CapteurVide vide;
    private Carte cartographie;
    private Sol[][] piece;
    private int posX,posY, nb_deplacement=0, memo=0;
    private Direction orientation;
    private ArrayList<Direction> toutDeplacement;

    public Robot(){
        batterie = new Batterie();
        reserve =new Reserve(100);
    }

    public Robot( Batterie batterie1, Sol[][] piece){

        toutDeplacement= new ArrayList<Direction>();
        this.piece=new Sol[piece.length][piece[0].length];
        base = new CapteurBase(this);
        collision = new CapteurCollision(this);
        vide = new CapteurVide(this);
        cartographie=new Carte();
        actif=true;
        batterie=batterie1;
        reserve=new Reserve(100);
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
        cartographie.getCarte()[posY][posX] = 2;
        thread= new Thread(this);
    }

    public Reserve getReserve(){return reserve;}
    public Batterie getBatterie(){return batterie;}
    public void setActif(Boolean actif){
        this.actif = actif;
    }


    /**
     * Déplace le robot manuellement
     * @param direction
     * @throws BatterieException
     */
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
                    piece[posY][posX].setEpaisseurPoussiere( piece[posY][posX].getEpaisseurPoussiere());
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
                cartographie.setInformation(posX, posY, 0);
                nb_deplacement++;
            }else{
                throw new BatterieException();
            }
        }
        else{
                switch (direction) {
                    case BAS:
                        cartographie.setInformation(posX, posY+1, 1);
                        break;
                    case HAUT:
                        cartographie.setInformation(posX, posY-1, 1);
                        break;
                    case DROITE:
                        cartographie.setInformation(posX+1, posY, 1);
                        break;
                    case GAUCHE:
                        cartographie.setInformation(posX-1, posY, 1);
                        break;
                }
                refuser = false;
        }
    }

    /**
     * Aspire la poussière
     * @param aspiration
     */
    private void aspirer(int aspiration){
        reserve.setReserveActuelle(aspiration);
    }

    public Direction getOrientation() {
        return orientation;
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

    /**
     * Méthode "Brute" pour retourner à la base
     */
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
        actif = false;
        System.exit(0);
    }


    /**
     * Déplace le robot en vérifiant le sac
     * @param direction
     */
    private void automateDeplacement(Direction direction){
            try {
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
                    if (!rempli) {
                        aspirer(piece[posY][posX].getEpaisseurPoussiere());
                        piece[posY][posX].setEpaisseurPoussiere(piece[posY][posX].getEpaisseurPoussiere());
                    }
                    if (piece[posY][posX].getSol() == typeSol.TAPIS) {
                        if (orientation == direction) {
                            batterie.consommation_obstacle();
                        } else
                            batterie.consommation_virage_sol();
                    } else if (piece[posY][posX].getSol() == typeSol.NORMAL) {
                        if (orientation == direction) {
                            batterie.consommation_normale();
                        } else {
                            batterie.consommation_virage();
                        }
                    }
                    toutDeplacement.add(direction);
                    orientation = direction;
                    if (cartographie.getCarte()[posY][posX] != 2)
                        cartographie.setInformation(posX, posY, 0);

                    nb_deplacement++;

                } catch(IllegalStateException e){
                    e.printStackTrace();
                }


    }

    /**
     * Choix de a direction par défaut à la sorti de la base
     * @return
     */
    private Direction choixAutomate(){
        collision.detecteur(Direction.HAUT);
        vide.detecteur(Direction.HAUT);

        if(refuser){
            refuser=false;
            cartographie.setInformation(posX, posY-1, 1);
            collision.detecteur(Direction.BAS);
            vide.detecteur(Direction.BAS);

            if(refuser){
                refuser=false;
                collision.detecteur(Direction.DROITE);
                vide.detecteur(Direction.DROITE);
                cartographie.setInformation(posX, posY+1, 1);

                if(refuser){
                    refuser=false;
                    collision.detecteur(Direction.GAUCHE);
                    vide.detecteur(Direction.GAUCHE);
                    cartographie.setInformation(posX-1, posY, 1);

                    if(refuser){
                        refuser=false;
                        cartographie.setInformation(posX+1, posY, 1);
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


    /**
     * permet de choisir la direction du robot. Il ira soit à gauche, soit tout droit.
     * Met également à jour les informations des capteurs vide et collision sur ça droite
     * memo va de 0 à 4, si la case est déjà parcouru, alors memo est incrémenté.
     * si memo atteint 4 on remet à 0. Cela signifi donc que le robot à vérifier autour de lui
     * @param direction
     * @return
     */
    private Direction choixAutomate(Direction direction) {
        boolean tampon =false;
        if(!simulerMouvement(direction)) {
            tampon = true;
        }


        collision.detecteur(direction);
        vide.detecteur(direction);
        if(refuser && !tampon){
            switch (direction){  //Cartographie à " l'impact " avec le mur d'en face
                case HAUT:
                        mettreAjourDroite(Direction.HAUT);
                        cartographie.setInformation(posX, posY - 1, 1);
                    refuser = false;
                    return Direction.GAUCHE;

                case BAS:
                        mettreAjourDroite(Direction.BAS);
                        cartographie.setInformation(posX, posY + 1, 1);
                    refuser = false;
                    return Direction.DROITE;

                case GAUCHE:
                        mettreAjourDroite(Direction.GAUCHE);
                        cartographie.setInformation(posX - 1, posY, 1);
                    refuser = false;
                    return Direction.BAS;

                case DROITE:
                        mettreAjourDroite(Direction.DROITE);
                        cartographie.setInformation(posX + 1, posY, 1);
                    refuser = false;
                    return Direction.HAUT;

            }
            return null;
        }
        else{
            if(!tampon) {
                mettreAjourDroite(direction);
                return direction;
            }
            else{
                switch (direction){
                    case GAUCHE:
                            orientation = Direction.BAS;
                        return Direction.BAS;

                    case DROITE:
                        orientation = Direction.HAUT;
                        return Direction.HAUT;

                    case BAS:
                        orientation = Direction.GAUCHE;
                        return Direction.GAUCHE;

                    case HAUT:
                        orientation = Direction.DROITE;
                        return Direction.DROITE;

                }
                return direction;
            }
        }

    }

    public void test(){
       int n=0;
        while(n<22){
            try {
                Thread.sleep(100);
            }catch(InterruptedException ie){
                ie.printStackTrace();
            }
            try{
                if(nb_deplacement==0) {
                    verificateActif();
                    automateDeplacement(choixAutomate());
                }
                else{
                    verificateActif();
                    verificateSac();
                    Direction direction;
                    direction = choixAutomate(orientation);
                    collision.detecteur(direction);
                    vide.detecteur(direction  );

                    if(refuser==false ) {

                        automateDeplacement(direction);
                    }

                }
                System.out.println("pos y "+posY+" pos x "+posX);


            }
            catch (BatterieException be) {
                be.printStackTrace();
                actif=false;
                break;
            }
            catch (ReserveException re){
                re.printStackTrace();
                retournerBase();
            }
            for(int i=0; i < cartographie.getCarte().length;i++ )
            {
                for(int j=0; j< cartographie.getCarte()[i].length; j++){
                    System.out.print(cartographie.getCarte()[i][j] );
                }
                System.out.println();
            }
            n++;
        }

    }

    @Override
    public void run(){
        while(true){
            if(actif)
            try {
                Thread.sleep(100);
            }catch(InterruptedException ie){
                ie.printStackTrace();
            }
            try{
                if(nb_deplacement==0) {
                    verificateActif();
                    automateDeplacement(choixAutomate());
                }
                else{
                    verificateActif();
                    verificateSac();
                    Direction direction;
                    direction = choixAutomate(orientation);
                    collision.detecteur(direction);
                    vide.detecteur(direction  );

                    if(refuser==false ) {
                        automateDeplacement(direction);
                    }
                }
                System.out.println("pos y "+posY+" pos x "+posX);
            }
            catch (BatterieException be) {
                be.printStackTrace();
                break;
            }
            catch (ReserveException re){
                re.printStackTrace();
                retournerBase();
            }

        }

    }


    /**
     * Verfi si le capteur à droite du robot détecte un obstacle ou le vide
     * @param direction
     */
    private void mettreAjourDroite(Direction direction){
        switch (direction) {
            case HAUT:
                collision.detecteur(Direction.DROITE);
                vide.detecteur(Direction.DROITE);
                if (refuser) {
                    refuser = false;
                    cartographie.setInformation(posX + 1, posY, 1);
                }
                break;
            case BAS:
                collision.detecteur(Direction.GAUCHE);
                vide.detecteur(Direction.GAUCHE);
                if(refuser) {
                    refuser=false;
                    cartographie.setInformation(posX - 1, posY, 1);
                }
                break;
            case DROITE:
                collision.detecteur(Direction.BAS);
                vide.detecteur(Direction.BAS);
                if(refuser) {
                    refuser=false;
                    cartographie.setInformation(posX, posY + 1, 1);
                }
                break;
            case GAUCHE:
                collision.detecteur(Direction.HAUT);
                vide.detecteur(Direction.HAUT);
                if(refuser) {
                    refuser=false;
                    cartographie.setInformation(posX, posY - 1, 1);
                }
                break;
        }
    }


    /**
     * Verifier si le mouvement suivant est sur une case déjà parcouru par le robot
     * @param direction
     * @return
     */
    private boolean simulerMouvement(Direction direction){
        switch(direction)
        {
            case BAS:
                if(cartographie.getCarte()[posY+1][posX]==0 || cartographie.getCarte()[posY+1][posX]==2 ) {
                return false;
            }
                break;
            case HAUT:
                if(cartographie.getCarte()[posY-1][posX]==0 || cartographie.getCarte()[posY-1][posX]==2) {
                return false;
            }
                break;
            case DROITE:
                if(cartographie.getCarte()[posY][posX+1]==0 || cartographie.getCarte()[posY][posX+1]==2) {
                return false;
            }
                break;
            case GAUCHE:
                if(cartographie.getCarte()[posY][posX-1]==0 || cartographie.getCarte()[posY][posX-1]==2) {
                return false;
            }
                break;
        }
        return true;
    }

    /**
     * Permet de connaitre la direction inverse à l'orientation du robot
     * cela empêche le robot de faire demi-tour alors qu'il est déjà passé
     * @return
     */
    private boolean inverse(Direction direction){
        switch (orientation){
            case DROITE:
                if(direction==Direction.GAUCHE)
                    return true;
                break;
            case GAUCHE:
                if(direction==Direction.DROITE)
                   return true;
                break;
            case BAS:
                if(direction==Direction.HAUT)
                    return true;
                break;
            case HAUT:
                if(direction==Direction.BAS)
                    return true;
                break;
        }
        return false;
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
            throw new ReserveException();

        }else {
            return rempli;
        }
    }


    public Thread getThread(){
        return thread;
    }


}