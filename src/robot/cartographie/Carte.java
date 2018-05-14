package robot.cartographie;


import java.util.Arrays;


public class Carte {
    private Boolean[][] carte;
    public Carte(){
        carte=new Boolean[100][100];
        Arrays.fill(carte, null);
    }

    public void setInformation(int ligne, int colonne, boolean obstacle){
            carte[ligne][colonne]=obstacle;
    }

    public Boolean[][] getCarte() {
        return carte;
    }
}
