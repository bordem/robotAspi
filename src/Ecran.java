

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
import sol.typeSol;


import static javafx.scene.input.TouchPoint.State.PRESSED;
import static robot.Direction.*;


//AFFICHAGE GRAPHIQUE SANS FXML
public class Ecran extends Application {



    private long debut = System.currentTimeMillis();
    private Scene scene;
    Group objet = new Group();


    private Node getSpecificNode(Parent root, String stg) {
        for (Node node : root.getChildrenUnmodifiable()) {
            if (stg.equals(node.getUserData())) {
                return node;
            }
        }
        return null;
    }



    @Override public void start(Stage stage) {

        Piece_in piece_in = new Piece_in();
        Donnee_Piece piece = new Donnee_Piece();
        piece.setPiece( piece_in.getArray() );
        piece.afficherPiece();
        Sol[][] sol = new Sol[piece.getPiece().length][piece.getPiece()[0].length];
        System.out.println(" ICI "+sol.length+" "+sol[0].length);
        for(int i=0; i< sol.length; i++)
        {
            for(int j=0;j<sol[i].length;j++){
                sol[i][j] = new Sol(piece.getPiece()[i][j]);

            }

        }
        Robot robot = new Robot(new Batterie(), sol);
        Scene scene = new Scene(objet,1000,600);

        Circle circle = new Circle(13,Color.CYAN);
        Canvas canvas = new Canvas();
        canvas.setLayoutX(440);
        canvas.setHeight(scene.getHeight());
        canvas.setWidth(scene.getWidth()-440);
        circle.setCenterX(455+robot.getX()*30);
        circle.setCenterY(15+robot.getY()*30);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        objet.getChildren().addAll(canvas, circle);
        for(int i=0; i< sol.length; i++)
        {
            for(int j=0;j<sol[i].length;j++){
                if(sol[i][j].getSol()== typeSol.OBSTACLE){
                    gc.setFill(Color.BLACK);
                }
                else if(sol[i][j].getSol()== typeSol.NORMAL){
                    gc.setFill(Color.ORANGE);
                    String key = String.valueOf(i)+String.valueOf(j);
                    String tampon = String.valueOf(sol[i][j].getEpaisseurPoussiere());
                    Label label=new Label(tampon);
                    label.setUserData(key);
                    label.setLayoutX(440+j*30);
                    label.setLayoutY(i*30);
                    objet.getChildren().add(label);
                }
                else if(sol[i][j].getSol()== typeSol.VIDE){
                    gc.setFill(Color.LIGHTGREY);
                }
                else if(sol[i][j].getSol()== typeSol.TAPIS){
                    gc.setFill(Color.LIGHTBLUE);
                    String key = String.valueOf(i)+String.valueOf(j);
                    String tampon = String.valueOf(sol[i][j].getEpaisseurPoussiere());
                    Label label=new Label(tampon);
                    label.setLayoutX(440+j*30);
                    label.setLayoutY(i*30);
                    label.setUserData(key);
                    objet.getChildren().add(label);
                }
                else if(sol[i][j].getSol()== typeSol.BASE){
                    gc.setFill(Color.ROYALBLUE);
                }
                gc.fillRect(j*30, i*30, 30, 30);
            }
        }



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
        //             Quantité poussière             //
        //////////////////////////////////////////////////
        Task poussiereAspire = new Task<Void>() {
            @Override public Void call() {
                while(true) {
                    if (isCancelled()) {
                        break;
                    }
                    //aspiration poussière
                    String mess="Le robot a aspiré  : "+robot.getReserve().getReserveActuelle() +" quantité de poussière";
                    updateMessage(mess);
                }
                return null;
            }
        };

        //////////////////////////////////////////////////
        //             Thread Compteur de temps         //
        //////////////////////////////////////////////////
        Task calculTemps = new Task<Void>() {
            @Override public Void call() {
                long temps,seconde,minute;
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

        final Button buttonStart = new Button("START");
        buttonStart.setMinHeight(50);
        buttonStart.setMinWidth(100);
        buttonStart.setLayoutY(y);
        buttonStart.setLayoutX(100);
        objet.getChildren().add(buttonStart);
        buttonStart.setOnAction(e->{
            //Il faut lier le robot au bouton
            //robot.start();
        });

        y =y+75;
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
        Text textNBBase= new Text(0,y,"Le robot est revenu a la base "+robot.getNb_deplacement()+" fois : ");
        textNBBase.setFont(new Font(12));
        objet.getChildren().add(textNBBase);

        //GRILLE PROGRESS BARRE
        Text textBatterie= new Text(0,y,"Batterie : ");
        textBatterie.setFont(new Font(12));
        GridPane.setConstraints(textBatterie, 0, 0);

        ProgressBar barBatterie = new ProgressBar();
        barBatterie.setLayoutX(100);
        barBatterie.setLayoutY(y);
        barBatterie.progressProperty().bind(compteur.progressProperty());
        GridPane.setConstraints(barBatterie, 1, 0);

        Text textReservoir= new Text(0,y,"Reservoir a poussière : ");
        textReservoir.setFont(new Font(12));
        GridPane.setConstraints(textReservoir, 0, 1);

        ProgressBar barReservoir = new ProgressBar();
        barReservoir.setLayoutX(100);
        barReservoir.setLayoutY(y);
        barReservoir.progressProperty().bind(calculTemps.progressProperty());
        GridPane.setConstraints(barReservoir, 1, 1);

        y = y+25;
        final GridPane grilleProgressBarre = new GridPane();

        grilleProgressBarre.setMaxWidth(300);
        grilleProgressBarre.setMaxHeight(50);
        grilleProgressBarre.setLayoutY(y);
        grilleProgressBarre.getColumnConstraints().setAll(
                new ColumnConstraints(200),
                new ColumnConstraints(100));
        grilleProgressBarre.getColumnConstraints().get(0).setHgrow(Priority.ALWAYS);
        grilleProgressBarre.getColumnConstraints().get(1).setHgrow(Priority.ALWAYS);
        grilleProgressBarre.getRowConstraints().setAll(
                new RowConstraints(25, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                new RowConstraints(25, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
        grilleProgressBarre.getRowConstraints().get(0).setVgrow(Priority.NEVER);
        grilleProgressBarre.getRowConstraints().get(1).setVgrow(Priority.ALWAYS);

        grilleProgressBarre.getChildren().addAll(textBatterie,barBatterie,textReservoir,barReservoir);
        objet.getChildren().add(grilleProgressBarre);

        //TEXTE
        y=y+100;
        Text textQuantitePoussiere= new Text(0,y,"");
        textQuantitePoussiere.textProperty().bind(poussiereAspire.messageProperty());
        textQuantitePoussiere.setFont(new Font(12));
        objet.getChildren().add(textQuantitePoussiere);


        //////////////////////////////////////////////////////
        //                 CONTROLE DU ROBOT                //
        //////////////////////////////////////////////////////
        y=y+25;
        final int hauteurControle=250;
        final int longeurControle=300;
        final int nbHauteur = 5;
        final int nbLargeur = 3;

        final Button buttonHaut = new Button("HAUT");
        GridPane.setConstraints(buttonHaut, 1, 0);
        buttonHaut.setMaxWidth(longeurControle/nbLargeur);
        buttonHaut.setMaxHeight(hauteurControle/nbHauteur);
        buttonHaut.setOnAction(e->{
            try
            {
                robot.deplacerRobot(Direction.HAUT);
                circle.setCenterX(455+robot.getX()*30);
                circle.setCenterY(15+robot.getY()*30);
                typeSol ceSol = sol[robot.getY()][robot.getX()].getSol();
                if( ceSol!=typeSol.BASE && ceSol!=typeSol.OBSTACLE && ceSol != typeSol.VIDE){
                    String key = String.valueOf(robot.getY())+String.valueOf(robot.getX());
                    Label lab = (Label)getSpecificNode(objet,key);
                    lab.setText(String.valueOf(sol[robot.getY()][robot.getX()].getEpaisseurPoussiere()));
                }
            }
            catch (BatterieException be)
            {
                be.printStackTrace();
            }
        });
        //objet.getChildren().add(buttonHaut);


        final Button buttonGauche = new Button("GAUCHE");
        GridPane.setConstraints(buttonGauche, 0, 1);
        buttonGauche.setMaxWidth(longeurControle/nbLargeur);
        buttonGauche.setMaxHeight(hauteurControle/nbHauteur);
        buttonGauche.setOnAction(e->{
            try {
                robot.deplacerRobot(Direction.GAUCHE);
                circle.setCenterX(455+robot.getX()*30);
                circle.setCenterY(15+robot.getY()*30);
                typeSol ceSol = sol[robot.getY()][robot.getX()].getSol();
                if( ceSol!=typeSol.BASE && ceSol!=typeSol.OBSTACLE && ceSol != typeSol.VIDE){
                    String key = String.valueOf(robot.getY())+String.valueOf(robot.getX());
                    Label lab = (Label)getSpecificNode(objet,key);
                    lab.setText(String.valueOf(sol[robot.getY()][robot.getX()].getEpaisseurPoussiere()));
                }
            }catch(BatterieException be){
                be.printStackTrace();
            }
        });
        //objet.getChildren().add(buttonGauche);

        final Button buttonDroit = new Button("DROIT");
        GridPane.setConstraints(buttonDroit, 2, 1);
        buttonDroit.setMaxWidth(longeurControle/nbLargeur);
        buttonDroit.setMaxHeight(hauteurControle/nbHauteur);
        buttonDroit.setOnAction(e->{
            try {
                robot.deplacerRobot(Direction.DROITE);
                circle.setCenterX(455+robot.getX()*30);
                circle.setCenterY(15+robot.getY()*30);
                typeSol ceSol = sol[robot.getY()][robot.getX()].getSol();
                if( ceSol!=typeSol.BASE && ceSol!=typeSol.OBSTACLE && ceSol != typeSol.VIDE){
                    String key = String.valueOf(robot.getY())+String.valueOf(robot.getX());
                    Label lab = (Label)getSpecificNode(objet,key);
                    lab.setText(String.valueOf(sol[robot.getY()][robot.getX()].getEpaisseurPoussiere()));
                }
            }catch (BatterieException be)
            {
                be.printStackTrace();
            }
        });
        //objet.getChildren().add(buttonDroit);


        final Button buttonBas = new Button("BAS");
        GridPane.setConstraints(buttonBas, 1, 2);
        buttonBas.setMaxWidth(longeurControle/nbLargeur);
        buttonBas.setMaxHeight(hauteurControle/nbHauteur);
        buttonBas.setOnAction(e->{
            try {
                robot.deplacerRobot(Direction.BAS);
                circle.setCenterX(455+robot.getX()*30);
                circle.setCenterY(15+robot.getY()*30);
                typeSol ceSol = sol[robot.getY()][robot.getX()].getSol();
                if( ceSol!=typeSol.BASE && ceSol!=typeSol.OBSTACLE && ceSol != typeSol.VIDE){
                    String key = String.valueOf(robot.getY())+String.valueOf(robot.getX());
                    Label lab = (Label)getSpecificNode(objet,key);
                    lab.setText(String.valueOf(sol[robot.getY()][robot.getX()].getEpaisseurPoussiere()));
                }
            }catch (BatterieException be){
                be.printStackTrace();
            }
        });
        //objet.getChildren().add(buttonBas);

        //GRILLE CONTROLE
        final GridPane grilleControle = new GridPane();

        grilleControle.setMaxWidth(longeurControle);
        grilleControle.setMaxHeight(hauteurControle);
        grilleControle.setLayoutY(y);

        grilleControle.getColumnConstraints().setAll(
                new ColumnConstraints(longeurControle/nbLargeur),
                new ColumnConstraints(longeurControle/nbLargeur),
                new ColumnConstraints(longeurControle/nbLargeur));
        grilleControle.getColumnConstraints().get(0).setHgrow(Priority.ALWAYS);
        grilleControle.getColumnConstraints().get(1).setHgrow(Priority.ALWAYS);
        grilleControle.getColumnConstraints().get(2).setHgrow(Priority.ALWAYS);

        grilleControle.getRowConstraints().setAll(
                new RowConstraints(hauteurControle/nbHauteur, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                new RowConstraints(hauteurControle/nbHauteur, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                new RowConstraints(hauteurControle/nbHauteur, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                new RowConstraints(hauteurControle/nbHauteur, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                new RowConstraints(hauteurControle/nbHauteur, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
        grilleControle.getRowConstraints().get(0).setVgrow(Priority.NEVER);
        grilleControle.getRowConstraints().get(1).setVgrow(Priority.NEVER);
        grilleControle.getRowConstraints().get(2).setVgrow(Priority.NEVER);
        grilleControle.getRowConstraints().get(3).setVgrow(Priority.NEVER);
        grilleControle.getRowConstraints().get(4).setVgrow(Priority.ALWAYS);


        objet.getChildren().add(grilleControle);

        //////////////////////////////////////////
        //            Bouton option             //
        //////////////////////////////////////////
        final Button boutonOption = new Button("Option");
        GridPane.setConstraints(boutonOption, 1, 4);
        boutonOption.setMaxWidth(longeurControle/nbLargeur);
        boutonOption.setMaxHeight(hauteurControle/nbHauteur);

        //objet.getChildren().add(boutonOption);

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


            final Scene scene2 = new Scene(root, 400, 100);
            stageOption.setTitle("Options robot Aspi");
            stageOption.setX(stage.getX()+(stage.getWidth()/2)-(stageOption.getWidth()/2));
            stageOption.setY(stage.getY()+(stage.getHeight()/2)-(stageOption.getWidth()/2));
            stageOption.setScene(scene2);
            stageOption.show();

        });

        grilleControle.getChildren().setAll(buttonBas, buttonHaut, buttonGauche,buttonDroit,boutonOption);



        //Image schema

        //new Thread(task).start();
        new Thread(calculTemps).start();
        new Thread(compteur).start();
        new Thread(poussiereAspire).start();

        //Icone pour l'application
        //String IconPath = "file:icone.png";
        //stage.getIcons().add(new Image(IconPath));
        stage.setTitle("Robot Aspi");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }


    public static void main(String[] args) {
        Application.launch(args);
    }
}