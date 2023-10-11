package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * ゲーム開始時の画面表示を管理するクラス
 */
public class StartController {

    @FXML
    private Button exitButton;

    @FXML
    private Button startButton;

    @FXML
    void onStartButtonAction(ActionEvent event) {
        Main.getInstance().selectView();
    }

    @FXML
    void onExitButtonAction(ActionEvent event) {
        Main.getInstance().close();
    }

}
