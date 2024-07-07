package org.example.bcn.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;

public class BCN_Controller {

    @FXML
    private ProgressBar ID_Progress; // Declaración privada de un progress bar denominado ID_Progress . se utiliza para la indentificación del componente a utilizar

    @FXML
    private Label progressLabel;

    @FXML
    private Pane ID_ContainerBOX;

    public ProgressBar getID_Progress() {
        return ID_Progress;
    }

    public Label getProgressLabel() {
        return progressLabel;
    }

    public Pane getID_ContainerBOX() {
        return ID_ContainerBOX;
    }
}