

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
import robot.Direction;
import robot.Reserve;
import robot.Robot;
import sol.Sol;

import static robot.Direction.*;

public class Ecran extends Application {



    private long debut = System.currentTimeMillis();
    private Scene scene;
    Group objet = new Group();

    @Override public void start(Stage stage) {

        Piece_in piece_in = new Piece_in();
        Donnee_Piece piece = new Donnee_Piece();
        piece.setPiece( piece_in.getArray() );
        piece.afficherPiece();
        Sol[][] sol = new Sol[piece.getPiece().length][piece.getPiece()[0].length];
        for(int i=0; i< sol.length; i++)
        {
            for(int j=0;j<sol[i].length;j++){
                sol[i][j] = new Sol(piece.getPiece()[i][j]);
                sol[i][j].afficherSol();
            }
            System.out.println("");
        }
        Robot robot = new Robot(new Reserve(100),new Batterie(), sol);

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
        //////////////////////////////////////////////////
        //             Compteur deplacement             //
        //////////////////////////////////////////////////
        Task compteur = new Task<Void>() {
            @Override public Void call() {
                                while(true) {
                    if (isCancelled()) {
                        break;
                    }
                    String mess="Le robot a parcouru une distance de : "+robot.getNb_deplacement();
                    updateMessage(mess);
                    //System.out.println(mess);
                }
                return null;
            }
        };
        //////////////////////////////////////////////////
        //             Thread Compteur de temps         //
        //////////////////////////////////////////////////
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
        //Compte le temps
        Text textTemps = new Text(0,y,"");
        textTemps.setFont(new Font(12));
        textTemps.textProperty().bind(calculTemps.messageProperty());
        objet.getChildren().add(textTemps);

        //Le robot a parcouru une distance de :
        y=y+25;
        Text textDeplacement= new Text(0,y,"");
        textDeplacement.setFont(new Font(12));
        textDeplacement.textProperty().bind(compteur.messageProperty());
        objet.getChildren().add(textDeplacement);

        y=y+25;
        Text textNBBase= new Text(0,y,"Le robot est revenu a la base n fois : ");
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
        //////////////////////////////////////////////////////
        //                 CONTROLE DU ROBOT                //
        //////////////////////////////////////////////////////
        y=y+25;
        final Button buttonHaut = new Button("HAUT");
        buttonHaut.setLayoutX(100);
        buttonHaut.setLayoutY(y);
        buttonHaut.setOnAction(e->{
            robot.deplacerRobot(Direction.HAUT);
        });
        objet.getChildren().add(buttonHaut);

        y=y+40;
        final Button buttonGauche = new Button("GAUCHE");
        buttonGauche.setLayoutX(25);
        buttonGauche.setLayoutY(y);
        buttonGauche.setOnAction(e->{
            robot.deplacerRobot(Direction.GAUCHE);
        });
        objet.getChildren().add(buttonGauche);

        final Button buttonDroit = new Button("DROIT");
        buttonDroit.setLayoutX(165);
        buttonDroit.setLayoutY(y);
        buttonDroit.setOnAction(e->{
            robot.deplacerRobot(Direction.DROITE);
        });
        objet.getChildren().add(buttonDroit);

        y=y+40;
        final Button buttonBas = new Button("BAS");
        buttonBas.setLayoutX(100);
        buttonBas.setLayoutY(y);
        buttonBas.setOnAction(e->{
            robot.deplacerRobot(Direction.BAS);
        });
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
            sliderBatterie.setValue(robot.getBatterie().getCapaciteMax());
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
        new Thread(compteur).start();

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