package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StartController {

    @FXML
    private Button exitButton;

    @FXML
    private Button startButton;

    @FXML
    void onStartButtonAction(ActionEvent event) {
        Main.getInstance().gameView();
    }

    @FXML
    void onExitButtonAction(ActionEvent event) {
        Main.getInstance().close();
    }

}
