import inout.Donnee_Piece;
import inout.Piece_in;
import robot.Batterie;
import robot.Direction;
import robot.Reserve;
import robot.Robot;
import sol.Sol;

public class Main {
    public static void main(String[] args){
        Piece_in piece_in = new Piece_in();
        Donnee_Piece piece = new Donnee_Piece();
        piece.setPiece( piece_in.getArray() );
        piece.afficherPiece();
        Sol[][] sol = new Sol[piece.getPiece().length][piece.getPiece()[0].length];
        System.out.println(" ICI "+sol.length+" "+sol[0].length);
        for(int i=0; i< sol.length; i++)
        {
            for(int j=0;j<sol[i].length;j++){
                sol[i][j] = new Sol(piece.getPiece()[i][j]);
                sol[i][j].afficherSol();
            }
            System.out.println("");
        }
        Robot robot = new Robot(new Reserve(100),new Batterie(), sol);
    }
}
