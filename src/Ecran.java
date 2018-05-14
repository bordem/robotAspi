
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Ecran extends Application {
    private long debut = System.currentTimeMillis();
    private Scene scene;
    Group objet = new Group();

    @Override public void start(Stage stage) {

        //System.out.println("Debut : "+debut);

        /*Task task = new Task<Void>() {
            @Override public Void call() {
                final int max = 100000000;
                for (int i=1; i<=max; i++) {
                    if(i==max){i=0;}
                    if (isCancelled()) {
                        break;
                    }
                    updateProgress(i, max);

                }
                return null;
            }
        };*/

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

        /*
            ProgressBar bar = new ProgressBar();
            bar.progressProperty().bind(task.progressProperty());
            objet.getChildren().add(bar);
        */

        Text textTemps = new Text(0,25,"");
        textTemps.setFont(new Font(12));
        textTemps.textProperty().bind(calculTemps.messageProperty());
        objet.getChildren().add(textTemps);

        Text textDeplacement= new Text(0,50,"Le robot a parcouru une distance de : ");
        textDeplacement.setFont(new Font(12));
        objet.getChildren().add(textDeplacement);

        //new Thread(task).start();
        new Thread(calculTemps).start();

        Scene scene = new Scene(objet,1000,600);

        stage.setTitle("RobotAspi");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }


    public static void main(String[] args) {
        Application.launch(args);
    }
}