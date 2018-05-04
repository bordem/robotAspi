package inout;

public class Donnee_Piece {
    private String[][] piece;
    public Donnee_Piece(String [][] piece){
        this.piece = piece;
    }

    public void setPiece(String[][] piece) {
        this.piece = piece;
    }

    public void setPosition(String objet, int x, int y){
        this.piece[x][y]=objet;
    }

    public String[][] getPiece() {
        return piece;
    }

    public String getSpecificObject(int x, int y){
        return piece[x][y];
    }
}
