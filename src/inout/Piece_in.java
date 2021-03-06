package inout;


import java.io.*;
import java.util.ArrayList;

public class Piece_in {
    final File f =new File("ressource/piece.txt");
    private String[][] array;
    public Piece_in() {
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
                        chaine = chaine +ligne.substring(i + 1, i + 2);
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
    private void transformerListe(ArrayList<ArrayList<String>> listeString ){
        array = new String[listeString.size()][listeString.get(0).size()];
        for(int i=0; i<listeString.size(); i++){
            for(int j=0; j<listeString.get(i).size();j++){
                array[i][j]=listeString.get(i).get(j);
            }
        }
    }

    public String[][] getArray() {
        return array;
    }
}
