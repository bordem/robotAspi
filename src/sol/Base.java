package sol;

import inout.Donnee_Piece;
import robot.Robot;
import robot.capteur.CapteurBase;



/*
 La base est le point de départ du robot dans la pièce.
 Quand la batterie du robot est pleinement chargée et son réservoir de poussière vidé,
    le robot peut redémarrer et continuer son ménage.
 Le temps qu’il faut au robot pour accomplir sa tâche sera chronométré.
 Ce temps doitaussi inclure les durées de chaque recharge et vidange de la poussière (actions parallèles mais à détailler)
 */
public class Base {
    //La base a un capteur de présence du robot.
    //Ce capteur est actif tant que le robot est positionné dessus.
    private CapteurBase capteur;
    private int CompteurBase;
    private Donnee_Piece piece;
    public Base(CapteurBase cap, Donnee_Piece piece) {
        capteur = cap;
        this.piece=piece;
    }

    public void rechargerRobot(Robot robot) {
        //Il faut 5 secondes pour recharger le robot

        /*if (capteur.getDetect() == true) {
            robot.getBatterie().rechargerBatterie();
        }*/



    }
    public void viderReservoir(Robot robot) {
        //Il faut 2 seconde pour recharger le robot
        /*if (capteur.getDetect() == true) {
            robot.getReserve().viderReseve();
        }*/

    }
}