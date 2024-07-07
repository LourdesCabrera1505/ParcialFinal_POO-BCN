package org.example.bcn.launcher;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.bcn.Controller.BCN_Controller;
import org.example.bcn.Controller.PreloaderProgress;

import java.io.IOException;

   //Esta clase launcher es responsable de inicializar la aplicación y lanzar una etapa principal
public class Launcher extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.initStyle(StageStyle.UNDECORATED); // se declara un estilo undercorate para quitar los bordes de la ventana de la aplicación
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/bcn/initPreloader.fxml")); // ruta de acceso donde se encuentra el form
        Parent root = loader.load(); // se carga el acceso root para cargar la vista
        BCN_Controller controller = loader.getController(); // se carga cualquier controlador del formulario Preloader
        primaryStage.setScene(new Scene(root, 550, 550));
        primaryStage.show();

        // Crea  una  nueva instancia de  PreloaderProgress  e inicia la animación
        PreloaderProgress animator = new PreloaderProgress(controller.getID_Progress(), controller.getProgressLabel(), primaryStage);
        animator.start();

    }

    public static void main(String[] args) throws IOException {
        launch(Launcher.class, args);
    }

}
