package robot.cartographie;


import java.util.Arrays;


public class Carte {
    private int[][] carte;
    public Carte(){
        carte=new int[6][6];
        for(int[] tableau : carte){
            Arrays.fill(tableau, 9);
        }
    }

    public void setInformation(int colonne, int ligne , int obstacle){
        carte[ligne][colonne]=obstacle;
    }

    public int[][] getCarte() {
        return carte;
    }
}
