package inout;


import java.io.*;

public class Piece_in {
    final File f =new File("ressource/piece.txt");
    public Piece_in() {
        Donnee_Piece piece = new Donnee_Piece();
        if (f.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String ligne;
                String chaine;
                int nligne = 0;

                while ((ligne = br.readLine()) != null) {
                    System.out.println("-" + nligne);
                    int ncolonne = 0;
                    for (int i = 0; i < ligne.length(); i=i+3) {
                        chaine = ligne.substring(i, i+1);
                        chaine = chaine +ligne.substring(i + 1, i + 2);
                        ncolonne++;
                    }
                    nligne++;
                }
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


}
