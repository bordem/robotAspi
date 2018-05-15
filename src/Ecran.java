

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import inout.Donnee_Piece;
import inout.Piece_in;
import robot.Batterie;
import robot.Reserve;
import robot.Robot;
import sol.Sol;

public class Ecran extends Application {
    Piece_in piece_in = new Piece_in();
    Donnee_Piece piece = new Donnee_Piece();
    piece.setPiece( piece_in.getArray() );
    piece.afficherPiece();
    Sol[][] sol = new Sol[piece.getPiece().length][piece.getPiece()[0].length];
    private Batterie batterie = new Batterie();
    private Reserve reserve = new Reserve(100);
    private Robot robot = new Robot(reserve,batterie,sol);
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
                    String mess="Temps écoulé depuis le premier démarrage du robot: "+((temps-debut)/1000);
                    updateMessage(mess);
                    //System.out.println(mess);
                }
                return null;
            }
        };

        int y =25;

        Text textTemps = new Text(0,y,"");
        textTemps.setFont(new Font(12));
        textTemps.textProperty().bind(calculTemps.messageProperty());
        objet.getChildren().add(textTemps);

        y=y+25;
        Text textDeplacement= new Text(0,y,"Le robot a parcouru une distance de : ");
        textDeplacement.setFont(new Font(12));
        objet.getChildren().add(textDeplacement);

        y=y+25;
        Text textNBBase= new Text(0,y,"Le robot est revenu a la bas n fois : ");
        textNBBase.setFont(new Font(12));
        objet.getChildren().add(textNBBase);

        y=y+25;
        Text textBatterie= new Text(0,y,"Batterie : ");
        textBatterie.setFont(new Font(12));
        objet.getChildren().add(textBatterie);
        ProgressBar barBatterie = new ProgressBar();
        barBatterie.setLayoutX(100);
        barBatterie.setLayoutY(y);
        objet.getChildren().add(barBatterie);

        y=y+40;
        Text textReservoir= new Text(0,y,"Reservoir a poussiere : ");
        textReservoir.setFont(new Font(12));
        objet.getChildren().add(textReservoir);
        y=y+10;
        ProgressBar barReservoir = new ProgressBar();
        barReservoir.setLayoutX(100);
        barReservoir.setLayoutY(y);
        objet.getChildren().add(barReservoir);

        y=y+50;
        Text textQuantitePoussiere= new Text(0,y,"Le robot a aspiré : ");
        textQuantitePoussiere.setFont(new Font(12));
        objet.getChildren().add(textQuantitePoussiere);

        y=y+25;
        final Button buttonHaut = new Button("HAUT");
        buttonHaut.setLayoutX(100);
        buttonHaut.setLayoutY(y);
        objet.getChildren().add(buttonHaut);

        y=y+40;
        final Button buttonGauche = new Button("GAUCHE");
        buttonGauche.setLayoutX(25);
        buttonGauche.setLayoutY(y);
        objet.getChildren().add(buttonGauche);
        final Button buttonDroit = new Button("DROIT");
        buttonDroit.setLayoutX(165);
        buttonDroit.setLayoutY(y);
        objet.getChildren().add(buttonDroit);

        y=y+40;
        final Button buttonBas = new Button("BAS");
        buttonBas.setLayoutX(100);
        buttonBas.setLayoutY(y);
        objet.getChildren().add(buttonBas);

        //////////////////////////////////////////
        //            Bouton option             //
        //////////////////////////////////////////
        y=y+50;
        final Button boutonOption = new Button("Option");
        boutonOption.setLayoutX(25);
        boutonOption.setLayoutY(y);
        objet.getChildren().add(boutonOption);

        //Si le bouton option est appuyé il ouvre une fenetre
        boutonOption.setOnAction((ActionEvent event) ->{
            Stage stageOption = new Stage();
            stageOption.show();

            //Groupe d'objet de la fenetre option
            Group objetOption = new Group();

            //Slider batterie avec label
            Text labelBatterie= new Text(0,25,"Batterie : ");
            labelBatterie.setFont(new Font(12));
            objetOption.getChildren().add(labelBatterie);

            Slider sliderBatterie = new Slider(0, 100, 1);
            sliderBatterie.setLayoutX(75);
            sliderBatterie.setLayoutY(0);
            sliderBatterie.setShowTickMarks(true);
            sliderBatterie.setShowTickLabels(true);
            //sliderBatterie.setValue();
            objetOption.getChildren().add(sliderBatterie);

            //Slider batterie avec label
            Text labelReserve= new Text(0,75,"Reserve : ");
            labelReserve.setFont(new Font(12));
            objetOption.getChildren().add(labelReserve);

            Slider sliderReserve = new Slider(0, 100, 1);
            sliderReserve.setLayoutX(75);
            sliderReserve.setLayoutY(50);
            sliderReserve.setShowTickMarks(true);
            sliderReserve.setShowTickLabels(true);
            objetOption.getChildren().add(sliderReserve);


            Scene sceneOption = new Scene(objetOption,300,100);
            stageOption.setTitle("Options robot Aspi");
            stageOption.setScene(sceneOption);
            stageOption.sizeToScene();
            stageOption.show();

        });


        //new Thread(task).start();
        new Thread(calculTemps).start();

        Scene scene = new Scene(objet,1000,600);

        stage.setTitle("Robot Aspi");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }


    public static void main(String[] args) {
        Application.launch(args);
    }
}