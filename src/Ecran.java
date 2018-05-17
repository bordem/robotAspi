

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import inout.Donnee_Piece;
import inout.Piece_in;
import robot.Batterie;
import robot.Direction;
import robot.Reserve;
import robot.Robot;
import robot.exception.BatterieException;
import sol.Sol;
import sun.font.TextLabel;

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
        //             Compteur             //
        //////////////////////////////////////////////////
        Task compteur = new Task<Void>() {
            @Override public Void call() {
                                while(true) {
                    if (isCancelled()) {
                        break;
                    }
                    //distance parcouru
                    String mess="Le robot a parcouru une distance de : "+robot.getNb_deplacement();
                    updateMessage(mess);
                    //ProgressBarre Batterie
                    double i,max;
                    i=robot.getBatterie().getCapaciteActuelle();
                    max=robot.getBatterie().getCapaciteMax();
                    updateProgress(i, max);

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
                long seconde;
                long minute;
                while(true) {
                    if (isCancelled()) {
                        break;
                    }
                    temps=((System.currentTimeMillis()-debut)/1000);
                    seconde = temps % 60;
                    minute = (temps-seconde)/60;
                    String mess="Temps écoulé depuis le premier démarrage du robot: "+minute+" min "+seconde+"sec";
                    updateMessage(mess);
                    //ProgressBarre Reserve
                    double i2,max2;
                    i2=robot.getReserve().getReserveActuelle();
                    max2=robot.getReserve().getReserveMax();
                    updateProgress(i2, max2);
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
        barBatterie.progressProperty().bind(compteur.progressProperty());
        objet.getChildren().add(barBatterie);

        y=y+40;
        Text textReservoir= new Text(0,y,"Reservoir a poussière : ");
        textReservoir.setFont(new Font(12));
        objet.getChildren().add(textReservoir);
        y=y+10;
        ProgressBar barReservoir = new ProgressBar();
        barReservoir.setLayoutX(100);
        barReservoir.setLayoutY(y);
        barReservoir.progressProperty().bind(calculTemps.progressProperty());
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
            try
            {
                robot.deplacerRobot(Direction.HAUT);
            }
            catch (BatterieException be)
            {
                be.printStackTrace();
            }
        });
        objet.getChildren().add(buttonHaut);

        y=y+40;
        final Button buttonGauche = new Button("GAUCHE");
        buttonGauche.setLayoutX(25);
        buttonGauche.setLayoutY(y);
        buttonGauche.setOnAction(e->{
            try {
                robot.deplacerRobot(Direction.GAUCHE);
            }catch(BatterieException be){
                be.printStackTrace();
            }
        });
        objet.getChildren().add(buttonGauche);

        final Button buttonDroit = new Button("DROIT");
        buttonDroit.setLayoutX(165);
        buttonDroit.setLayoutY(y);
        buttonDroit.setOnAction(e->{
            try {
                robot.deplacerRobot(Direction.DROITE);
            }catch (BatterieException be)
            {
                be.printStackTrace();
            }
        });
        objet.getChildren().add(buttonDroit);

        y=y+40;
        final Button buttonBas = new Button("BAS");
        buttonBas.setLayoutX(100);
        buttonBas.setLayoutY(y);
        buttonBas.setOnAction(e->{
            try {
                robot.deplacerRobot(Direction.BAS);
            }catch (BatterieException be){
                be.printStackTrace();
            }
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

            //BATTERIE
            final Label labelBatterie = new Label("Batterie : ");
            GridPane.setConstraints(labelBatterie, 0, 0);
            final Slider sliderBatterie = new Slider();
            GridPane.setConstraints(sliderBatterie, 1, 0);
            sliderBatterie.setShowTickMarks(true);
            sliderBatterie.setShowTickLabels(true);
            sliderBatterie.setValue(robot.getBatterie().getCapaciteMax());

            sliderBatterie.valueProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue<? extends Number> ov,
                                    Number old_val, Number new_val) {
                    robot.getBatterie().setCapaciteMax(new_val.doubleValue());
                }
            });

            //RESERVE
            final Label labelReserve= new Label("Reserve : ");
            GridPane.setConstraints(labelReserve, 0, 1);
            Slider sliderReserve = new Slider(0, 100, 1);
            GridPane.setConstraints(sliderReserve, 1, 1);
            sliderReserve.setShowTickMarks(true);
            sliderReserve.setShowTickLabels(true);
            sliderReserve.setValue(robot.getReserve().getReserveMax());

            sliderReserve.valueProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue<? extends Number> ov,
                                    Number old_val, Number new_val) {
                    robot.getReserve().setReserveMax(new_val.intValue());
                }
            });

            //GRILLE
            final GridPane root = new GridPane();
            root.setMaxWidth(200);

            root.getColumnConstraints().setAll(
                    new ColumnConstraints(100),
                    new ColumnConstraints(200));
                    root.getColumnConstraints().get(0).setHgrow(Priority.ALWAYS);
            root.getColumnConstraints().get(1).setHgrow(Priority.ALWAYS);

            root.getRowConstraints().setAll(
                    new RowConstraints(25, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                    new RowConstraints(25, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
            root.getRowConstraints().get(0).setVgrow(Priority.NEVER);
            root.getRowConstraints().get(1).setVgrow(Priority.ALWAYS);
            root.getChildren().setAll(labelBatterie, sliderBatterie, labelReserve,sliderReserve);


            final Scene scene = new Scene(root, 400, 100);
            stageOption.setTitle("Options robot Aspi");
            stageOption.setScene(scene);
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