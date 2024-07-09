package org.example.bcn.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class PreloaderProgress {
        private final ProgressBar progressBar;
        private final Label progressLabel;
        private final String[] modules;
        private final Stage primaryStage;
        private final BCN_Controller controller;

        private Scene previousScene;


        public PreloaderProgress(ProgressBar progressBar, Label progressLabel, Stage primaryStage, BCN_Controller controller) {
            this.progressBar = progressBar;
            this.progressLabel = progressLabel;
            this.primaryStage= primaryStage;
            this.controller = controller;
            this.modules = new String[] {
                    "Module 1", "Module 2", "Module 3",
                    "Module 4", "Module 5", "Module 6",
                    "Module 7",
            };
        }

        /**
         *  Este método inicia la animación de la barra de progreso y el texto de carga
         *  se crea una línea de tiempo con multiples fotogramas clave para animar la barra de progreso
         *  se cambia el estilo de la barra de progreso y actualiza el texto de carga
         * */

        public void start () {
            Timeline timeline = new Timeline(); // Se genera una nueva línea de tiempo
            // se recorre cada modulp
            for (int i = 0; i < modules.length; i++) {
                final double progress = (i + 1) / (double) modules.length;// se calcula el progreso para el módulo actual
                final String module = modules[i]; // se obtiene el nombre del módulo actual (para el texto de carga)
                KeyFrame kfProgress = new KeyFrame(Duration.seconds(i * 0.9), // KeyFrame para cambiar el progreso de la barra de progreso en el tiempo especificado (0.9 segundos) y
                        new KeyValue(progressBar.progressProperty(), progress)); // actualizar el progreso en la barra de progreso

                // KeyFrame para cambiar el estilo de la barra de progreso
                KeyFrame kfStyle = new KeyFrame(Duration.seconds(i * 0.5),
                        new KeyValue(progressBar.styleProperty(), "-fx-accent: derive(red, " + (i % 2 == 0 ? "100%" : "-100%") + ");"));

                //key frame para cambiar el texto de carga
                KeyFrame kfLabel = new KeyFrame(Duration.seconds(i * 0.9),
                        new KeyValue(progressLabel.textProperty(), " Loading " + module + "..."));


                timeline.getKeyFrames().addAll(kfProgress,kfStyle, kfLabel); //añade los fotogramas a la línea de tiempo
            }

            // Establece una acción a realizar cuando la línea de tiempo finalice
            timeline.setOnFinished(e -> {
                progressLabel.setText("Finished loading");
                Platform.runLater(this::HomeFXML);

            });
            // Establece el número de repetincion que tendra la barra de progreso
            timeline.setCycleCount(1);
            // Establece la línea de tiempo para revertir la dirección después de cada ciclo
            timeline.setAutoReverse(true);
            // Inicia la animación de la línea de tiempo de la barra de progreso y el texto de carga
            timeline.play();

         }

        private void HomeFXML() {
            switchToScene("/org/example/bcn/Home.fxml");
        }

        private void switchToScene(String fxmlPath) {
            try {
                previousScene = primaryStage.getScene();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent root  = loader.load();
                Scene newScene = new Scene(root);
                primaryStage.setScene(newScene);
                primaryStage.centerOnScreen();
                String css = getClass().getResource("/css/bcnStyle.css").toExternalForm();
                newScene.getStylesheets().add(css);
                primaryStage.show();

                setupSceneHandlers(newScene);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private void switchToPreviousScene () {
            if(previousScene != null) {
                primaryStage.setScene(previousScene);
                primaryStage.centerOnScreen();
                primaryStage.show(); // Mostrar la escena anterior
            }
        }

    private void setupSceneHandlers(Scene scene) {
        Button openClientRegister = (Button) scene.lookup("#ClientsBox");
        if (openClientRegister != null) {
            openClientRegister.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/bcn/ClientesForm.fxml"));
                    Parent root = loader.load();
                    Scene clientsScene = new Scene(root);
                    primaryStage.setScene(clientsScene);
                    primaryStage.centerOnScreen();
                    primaryStage.show();

                    Button back = (Button) clientsScene.lookup("#RetornButton");
                    if (back != null) {
                        back.setOnAction(event1 -> switchToScene("/org/example/bcn/Home.fxml"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        Button reportes = (Button) scene.lookup("#Transactions");
        if (reportes != null) {
            reportes.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/bcn/ReportesForm.fxml"));
                    Parent root = loader.load();
                    Scene reportScene = new Scene(root);
                    primaryStage.setScene(reportScene);
                    primaryStage.centerOnScreen();
                    primaryStage.show();

                    Button back = (Button) reportScene.lookup("#IDBack");
                    if (back != null) {
                        back.setOnAction(event1 -> switchToScene("/org/example/bcn/Home.fxml"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        Button cards = (Button) scene.lookup("#CardsBox");
        if(cards != null) {
            cards.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/bcn/CardsForm.fxml"));
                    Parent root = loader.load();
                    Scene cardsScene = new Scene(root);
                    primaryStage.setScene(cardsScene);
                    primaryStage.centerOnScreen();
                    primaryStage.show();

                    Button back = (Button) cardsScene.lookup("#ComeBack");
                    if (back!= null) {
                        back.setOnAction(event1 -> switchToScene("/org/example/bcn/Home.fxml"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        Button shops = (Button) scene.lookup("#SalesShop");
        if (shops!=null) {
            shops.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/bcn/Compras.fxml"));
                    Parent root = loader.load();
                    Scene salesScene = new Scene(root);
                    primaryStage.setScene(salesScene);
                    primaryStage.centerOnScreen();
                    primaryStage.show();

                    Button back = (Button) salesScene.lookup("#Backdown");
                    if (back!= null) {
                        back.setOnAction(event1 -> switchToScene("/org/example/bcn/Home.fxml"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
