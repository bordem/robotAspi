package robot.cartographie;


import java.util.Arrays;


public class Carte {
    private Boolean[][] carte;
    public Carte(){
        carte=new Boolean[100][100];
    }

    public void setInformation(int ligne, int colonne, boolean obstacle){
        carte[ligne][colonne]=new Boolean(obstacle);
        System.out.println("erferzstrh "+ carte[ligne][colonne]);
    }

    public Boolean[][] getCarte() {
        return carte;
    }
}