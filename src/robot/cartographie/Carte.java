package robot.cartographie;


import java.util.Arrays;


public class Carte {
    private Boolean[][] carte;
    public Carte(){
        carte=new Boolean[100][100];
    }

    public void setInformation(int colonne, int ligne , boolean obstacle){
        carte[ligne][colonne]=new Boolean(obstacle);
    }

    public Boolean[][] getCarte() {
        return carte;
    }
}
