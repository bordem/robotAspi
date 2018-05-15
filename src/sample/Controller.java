package sample;

import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Controller {

    private long debut = System.currentTimeMillis();
    Group objet = new Group();



    public void fonction(){
        Task calculTemps = new Task<Void>() {
            @Override public Void call() {
                long temps;
                while(true) {
                    if (isCancelled()) {
                        break;
                    }
                    temps=System.currentTimeMillis();
                    String mess="Temps écoulé depuis le premier démarrage du robot: "+((temps-debut)/1000);///1000000000;
                    updateMessage(mess);
                    //System.out.println(mess);
                }
                return null;
            }
        };
        Text textTemps = new Text(0,25,"");
        textTemps.setFont(new Font(12));
        textTemps.textProperty().bind(calculTemps.messageProperty());
        objet.getChildren().add(textTemps);
        Text textDeplacement= new Text(0,50,"Le robot a parcouru une distance de : ");
        textDeplacement.setFont(new Font(12));
        objet.getChildren().add(textDeplacement);

        //new Thread(task).start();
        new Thread(calculTemps).start();
    }


}
