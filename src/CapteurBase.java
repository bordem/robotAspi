import robot.Capteur;

public class CapteurBase extends Capteur{
    //Ce capteur est actif tant que le robot est positionné dessus
    //Quand le capteur change d’état pour devenir actif,
    //  la base émet un signal au robot pour qu’il se mette en recharge et vide son réservoir de poussière (ce sont des opérations simultanées)

}