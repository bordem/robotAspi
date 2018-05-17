

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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
import sol.typeSol;

import static robot.Direction.*;


//AFFICHAGE GRAPHIQUE SANS FXML
public class Ecran /*extends Application*/ {



   /* private long debut = System.currentTimeMillis();
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
        gc.setFill(Color.YELLOW);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        objet.getChildren().addAll(canvas, circle);
        for(int i=0; i< sol.length; i++)
        {
            for(int j=0;j<sol[i].length;j++){
                if(sol[i][j].getSol()== typeSol.OBSTACLE){
                    gc.setFill(Color.RED);
                }
                else if(sol[i][j].getSol()== typeSol.NORMAL){
                    gc.setFill(Color.WHITE);
                    String key = String.valueOf(i)+String.valueOf(j);
                    String tampon = String.valueOf(sol[i][j].getEpaisseurPoussiere());
                    Label label=new Label(tampon);
                    label.setUserData(key);
                    label.setLayoutX(440+j*30);
                    label.setLayoutY(i*30);
                    objet.getChildren().add(label);
                }
                else if(sol[i][j].getSol()== typeSol.VIDE){
                    gc.setFill(Color.BLUE);
                }
                else if(sol[i][j].getSol()== typeSol.TAPIS){
                    gc.setFill(Color.BROWN);
                    String key = String.valueOf(i)+String.valueOf(j);
                    String tampon = String.valueOf(sol[i][j].getEpaisseurPoussiere());
                    Label label=new Label(tampon);
                    label.setLayoutX(440+j*30);
                    label.setLayoutY(i*30);
                    label.setUserData(key);
                    objet.getChildren().add(label);
                }
                else if(sol[i][j].getSol()== typeSol.BASE){
                    gc.setFill(Color.GREEN);
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
        objet.getChildren().add(buttonHaut);

        y=y+40;
        final Button buttonGauche = new Button("GAUCHE");
        buttonGauche.setLayoutX(25);
        buttonGauche.setLayoutY(y);
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
        objet.getChildren().add(buttonGauche);

        final Button buttonDroit = new Button("DROIT");
        buttonDroit.setLayoutX(165);
        buttonDroit.setLayoutY(y);
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
        objet.getChildren().add(buttonDroit);

        y=y+40;
        final Button buttonBas = new Button("BAS");
        buttonBas.setLayoutX(100);
        buttonBas.setLayoutY(y);
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

            sliderBatterie.valueProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue<? extends Number> ov,
                                    Number old_val, Number new_val) {
                    robot.getBatterie().setCapaciteMax(new_val.doubleValue());
                }
            });

            objetOption.getChildren().add(sliderBatterie);

            //Slider reserve avec label
            Text labelReserve= new Text(0,75,"Reserve : ");
            labelReserve.setFont(new Font(12));
            objetOption.getChildren().add(labelReserve);

            Slider sliderReserve = new Slider(0, 100, 1);
            sliderReserve.setLayoutX(75);
            sliderReserve.setLayoutY(50);
            sliderReserve.setShowTickMarks(true);
            sliderReserve.setShowTickLabels(true);
            sliderReserve.setValue(robot.getReserve().getReserveMax());

            sliderReserve.valueProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue<? extends Number> ov,
                                    Number old_val, Number new_val) {
                    robot.getReserve().setReserveMax(new_val.intValue());
                }
            });

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



        stage.setTitle("Robot Aspi");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }


    public static void main(String[] args) {
        Application.launch(args);
    }*/
}