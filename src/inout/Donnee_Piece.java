package inout;

public class Donnee_Piece {

    private String[][] piece;
    public Donnee_Piece(){
    }

    public void setPiece(String[][] piece) {
        this.piece = piece;
    }

    public void setPosition(String objet, int x, int y){
        this.piece[x][y]=new String();
        this.piece[x][y]=objet;
    }

    public String[][] getPiece() {
        return piece;
    }

    public String getSpecificObject(int x, int y){
        return piece[x][y];
    }

    public void afficherPiece(){
       int i =0,j=0;
       for(String[] ligne : piece){
           for(String colonne : ligne){
               System.out.print(colonne);
               j++;
           }
           System.out.println();
           i++;
       }
    }
}
