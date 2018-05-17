package sample;

import inout.Donnee_Piece;
import inout.Piece_in;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import robot.Batterie;
import robot.Direction;
import robot.Robot;
import robot.exception.BatterieException;
import sol.Sol;
import sol.typeSol;

import java.awt.*;

public class Controller {

    private long debut = System.currentTimeMillis();
    @FXML
    private Group root;
    @FXML
    private Canvas canvas;
    private GraphicsContext gc ;
    @FXML
    private Button Haut;
    @FXML
    private Button Bas;
    @FXML
    private Button Droite;
    @FXML
    private Button Gauche;
    @FXML
    private Button Option;
    @FXML
    private Button Demarrer;
    @FXML
    private Text textTemps;
    @FXML
    private Text textDeplacement;
    @FXML
    private Text textBatterie;
    @FXML
    private Text textReservoir;
    @FXML
    private Text textNBBase;
    @FXML
    private Text textQuantitePoussiere;

    private ProgressBar barReservoir;

    private ProgressBar barBatterie;

    @FXML
    private Circle circle;

    private Piece_in piece_in;
    private Donnee_Piece piece;
    private Sol[][] sol;
    private Robot robot=new Robot();

    private Node getSpecificNode(Parent root, String stg) {
        for (Node node : root.getChildrenUnmodifiable()) {
            if (stg.equals(node.getUserData())) {
                return node;
            }
        }
        return null;
    }


    /**
     * Si on active, alors on instancie toutes les informations primordiales
     * permettant de débuter le traitement (Robot, piece,etc.)
     * quelques informations tel que le temps et le nombre de déplacement
     * sont initialisés
     * @param event
     */
    @FXML
    public void initialiser(ActionEvent event){
        gc = canvas.getGraphicsContext2D();
        piece_in = new Piece_in();
        Donnee_Piece piece = piece = new Donnee_Piece();
        piece.setPiece( piece_in.getArray() );
        piece.afficherPiece();
         sol = new Sol[piece.getPiece().length][piece.getPiece()[0].length];
        System.out.println(" ICI "+sol.length+" "+sol[0].length);
        for(int i=0; i< sol.length; i++)
        {
            for(int j=0;j<sol[i].length;j++){
                sol[i][j] = new Sol(piece.getPiece()[i][j]);
            }
        }
        robot = new Robot(new Batterie(), sol);
        circle.setLayoutX(455+robot.getX()*30);
        circle.setLayoutY(15+robot.getY()*30);
        circle.setFill(Color.CYAN);
        drawScene(gc);
        //////////////////////////////////////////////////
        //             Compteur                         //
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

        textTemps.textProperty().bind(calculTemps.messageProperty());
        textDeplacement.textProperty().bind(compteur.messageProperty());
        barBatterie = new ProgressBar();
        barBatterie.setLayoutX(100);
        barBatterie.setLayoutY(100);
        barBatterie.progressProperty().bind(compteur.progressProperty());
        root.getChildren().add(barBatterie);

        barReservoir = new ProgressBar();
        barReservoir.setLayoutX(100);
        barReservoir.setLayoutY(143);
        barReservoir.progressProperty().bind(calculTemps.progressProperty());
        root.getChildren().add(barReservoir);

        new Thread(calculTemps).start();
        new Thread(compteur).start();
    }

    /**
     * Prend en entrée un graphics context et dessine la pièce où se situe le robot
     * sur la droite de la fenêtre
     * Rouge correspond à un obstacle
     * Jaune correspond à la zone inexistante
     * Blanc correspond au sol normal
     * Bleu foncé correspond au vide
     * Marron correspond au tapis
     * Vert correspond à la base
     * @param gc
     */
    private void drawScene(GraphicsContext gc){
        gc.setFill(Color.YELLOW);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
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
                    root.getChildren().add(label);
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
                    root.getChildren().add(label);
                }
                else if(sol[i][j].getSol()== typeSol.BASE){
                    gc.setFill(Color.GREEN);
                }
                gc.fillRect(j*30, i*30, 30, 30);
            }
        }
    }

    @FXML
    public void optionEvent(ActionEvent event){

    }


    /**
     * Les boutons "buttonDirection X " sont les boutons qui permettent
     * au consommateur de guider le robot vers la position X
     * @param event
     */
    @FXML
    public void buttonDirectionBas(ActionEvent event){
        try
        {
            robot.deplacerRobot(Direction.BAS);
            circle.setLayoutX(455+robot.getX()*30);
            circle.setLayoutY(15+robot.getY()*30);
            typeSol ceSol = sol[robot.getY()][robot.getX()].getSol();
            if( ceSol!=typeSol.BASE && ceSol!=typeSol.OBSTACLE && ceSol != typeSol.VIDE){
                String key = String.valueOf(robot.getY())+String.valueOf(robot.getX());
                Label lab = (Label)getSpecificNode(root,key);
                lab.setText(String.valueOf(sol[robot.getY()][robot.getX()].getEpaisseurPoussiere()));
            }
        }
        catch (BatterieException be)
        {
            be.printStackTrace();
        }
    }

    @FXML
    public void buttonDirectionHaut(ActionEvent event){
        try
        {
            robot.deplacerRobot(Direction.HAUT);
            circle.setLayoutX(455+robot.getX()*30);
            circle.setLayoutY(15+robot.getY()*30);
            typeSol ceSol = sol[robot.getY()][robot.getX()].getSol();
            if( ceSol!=typeSol.BASE && ceSol!=typeSol.OBSTACLE && ceSol != typeSol.VIDE){
                String key = String.valueOf(robot.getY())+String.valueOf(robot.getX());
                Label lab = (Label)getSpecificNode(root,key);
                lab.setText(String.valueOf(sol[robot.getY()][robot.getX()].getEpaisseurPoussiere()));
            }
        }
        catch (BatterieException be)
        {
            be.printStackTrace();
        }
    }

    @FXML
    public void buttonDirectionDroite(ActionEvent event){
        try
        {
            robot.deplacerRobot(Direction.DROITE);
            circle.setLayoutX(455+robot.getX()*30);
            circle.setLayoutY(15+robot.getY()*30);
            typeSol ceSol = sol[robot.getY()][robot.getX()].getSol();
            if( ceSol!=typeSol.BASE && ceSol!=typeSol.OBSTACLE && ceSol != typeSol.VIDE){
                String key = String.valueOf(robot.getY())+String.valueOf(robot.getX());
                Label lab = (Label)getSpecificNode(root,key);
                lab.setText(String.valueOf(sol[robot.getY()][robot.getX()].getEpaisseurPoussiere()));
            }
        }
        catch (BatterieException be)
        {
            be.printStackTrace();
        }
    }

    @FXML
    public void buttonDirectionGauche(ActionEvent event){
        try
        {
            robot.deplacerRobot(Direction.GAUCHE);
            circle.setLayoutX(455+robot.getX()*30);
            circle.setLayoutY(15+robot.getY()*30);
            typeSol ceSol = sol[robot.getY()][robot.getX()].getSol();
            if( ceSol!=typeSol.BASE && ceSol!=typeSol.OBSTACLE && ceSol != typeSol.VIDE){
                String key = String.valueOf(robot.getY())+String.valueOf(robot.getX());
                Label lab = (Label)getSpecificNode(root,key);
                lab.setText(String.valueOf(sol[robot.getY()][robot.getX()].getEpaisseurPoussiere()));
            }
        }
        catch (BatterieException be)
        {
            be.printStackTrace();
        }
    }


    /**
     * Si le bouton option est appuyé il ouvre une fenetre d'options
     */
    @FXML
    public void configuration() {

        Stage stageOption = new Stage();
        stageOption.show();

        //Groupe d'objet de la fenetre option
        Group objetOption = new Group();

        //Slider batterie avec label
        Text labelBatterie = new Text(0, 25, "Batterie : ");
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
        Text labelReserve = new Text(0, 75, "Reserve : ");
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


        Scene sceneOption = new Scene(objetOption, 300, 100);
        stageOption.setTitle("Options robot Aspi");
        stageOption.setScene(sceneOption);
        stageOption.sizeToScene();
        stageOption.show();

    }
}
