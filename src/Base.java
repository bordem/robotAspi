import robot.Capteur;
import robot.Robot;

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

    public Base(CapteurBase cap) {
        capteur = cap;
    }

    public void rechargerRobot(Robot robot) {
        //Il faut 5 secondes pour recharger le robot
        if (capteur.getEtat() == true) {
            //robot.getBatterie().rechargerBatterie();
        }

    }
    public void viderReservoir(Robot robot) {
        //Il faut 2 seconde pour recharger le robot
        if (capteur.getEtat() == true) {
            robot.getReserve().viderReseve();
        }
    }
}