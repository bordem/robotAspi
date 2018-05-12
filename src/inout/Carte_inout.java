package inout;

import robot.cartographie.Carte;

import java.io.*;
import java.util.ArrayList;

public class Carte_inout {
    private final File f=new File("ressource/carte.txt");
    private Carte carte;
    public Carte_inout(){ }
    public void recupererCarte(){
        ArrayList<ArrayList<String>> listeString = new ArrayList<ArrayList<String>>();
        if (f.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String ligne;
                String chaine;
                int nligne = 0;

                while ((ligne = br.readLine()) != null) {
                    System.out.println("-" + nligne);
                    int ncolonne = 0;
                    ArrayList<String> tempo = new ArrayList<String>();
                    for (int i = 0; i < ligne.length(); i=i+3) {
                        chaine = ligne.substring(i, i+1);
                        tempo.add(chaine);
                        ncolonne++;
                    }
                    listeString.add(tempo);
                    nligne++;
                }
                transformerListe(listeString);
                br.close();
            } catch (FileNotFoundException fne) {
                fne.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else
        {
            System.out.println("Le fichier est inexistant");
        }
    }
    public void transformerListe(ArrayList<ArrayList<String>> listeString){

        for(int i=0; i<listeString.size(); i++){
            for(int j=0; j<listeString.get(i).size();j++){
                if(listeString.get(i).get(j).charAt(0)=='1' ){
                    carte.setInformation(i,j,true);
                }
                else if(listeString.get(i).get(j).charAt(0)=='0' ){
                    carte.setInformation(i,j,false);
                }
                //Sinon erreur
            }
        }
    }

    public Carte getCarte() {
        return carte;
    }

    public void setCarte(Carte carte){
        this.carte=carte;
    }
}
