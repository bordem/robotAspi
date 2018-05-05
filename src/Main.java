import inout.Donnee_Piece;
import inout.Piece_in;

public class Main {
    public static void main(String[] args){

        Piece_in piece_in = new Piece_in();
        Donnee_Piece piece = new Donnee_Piece();
        piece.setPiece( piece_in.getArray() );
    }
}
