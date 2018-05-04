package robot;

public class Capteur {
    private boolean detect;

    public Capteur(){
        detect=false;
    }
    public boolean getEtat(){
        return detect;
    }
    public void setEtat(boolean nouvelleEtat ){
        detect= nouvelleEtat ;
    }
}
