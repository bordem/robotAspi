package robot.exception;

public class ReserveException extends Exception {
    public ReserveException(){
        super("Réserve pleine");
    }
}
