package robot.capteur;

import robot.Robot;
import robot.capteur.Capteur;
import sol.Sol;

public class CapteurBase extends Capteur{
    //Ce capteur est actif tant que le robot est positionné dessus
    //Quand le capteur change d’état pour devenir actif,
    //  la base émet un signal au robot pour qu’il se mette en recharge et vide son réservoir de poussière (ce sont des opérations simultanées)
    //Quand le robot revient de la base, on écrit la topologie de la piece dans un fichier


    public CapteurBase(Robot robot)
    {
        super(robot);
    }

    @Override
    protected void setDetect(int x, int y, Sol[][] piece){}



}
