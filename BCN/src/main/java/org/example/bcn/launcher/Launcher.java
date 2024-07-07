package org.example.bcn.launcher;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

   //Esta clase launcher es responsable de inicializar la aplicación y lanzar una etapa principal
public class Launcher extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/bcn/initPreloader.fxml"));
        primaryStage.setTitle("JavaFX Application");
        primaryStage.setScene(new Scene(root, 550, 550));
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {
        launch(Launcher.class, args);
    }

}
