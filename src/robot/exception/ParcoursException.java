package robot.exception;

public class ParcoursException extends Exception {
    public ParcoursException(){
        super("Le robot a déjà parcouru toutes les cases aux alentours");
    }
}
