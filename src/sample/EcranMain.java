package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class EcranMain extends Application {



    @Override public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            primaryStage.setTitle("Robot Aspirateur");
            primaryStage.setScene(new Scene(root, 1000, 800));
            primaryStage.show();
        }catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }


    public static void main(String[] args) {

        Application.launch(args);
    }
}