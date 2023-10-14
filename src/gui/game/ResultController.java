package gui.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import quiz.QuizGame;

import gui.Main;

/**
 * クイズ結果の画面表示・管理を行うクラス
 */
public class ResultController {

    /** 結果を表示する画面 */
    @FXML
    private VBox resultPane;

    /** クイズ結果を表示するラベル */
    @FXML
    private Label resultLabel;

    /** クイズ再挑戦ボタン */
    @FXML
    private Button titleButton;

    /** ジャンル変更ボタン */
    @FXML
    private Button changeCategoryButton;

    /** ゲーム終了ボタン */
    @FXML
    private Button exitButton;
    
    /**
     * クイズ画面の表示内容を更新する
     */
    void updatePane(QuizGame quizGame) {
        quizGame = Game.getGame().getQuizGame();
        resultLabel.setText(
                String.format("正解数は %d 問中、 %d 問です。", 
                        quizGame.getCurrentQuestionNum(), quizGame.getCurrentAnswerNum()));
    }
    
    /**
     * 「タイトルに戻る」ボタンが押されたときの動作処理
     * @param event イベントハンドラ
     */
    @FXML
    void onTitleButtonAction(ActionEvent event) {
        Main.getInstance().showScene("Start");
    }

    /**
     * ジャンル選択ボタンが押されたときの動作処理
     * @param event イベントハンドラ
     */
    @FXML
    void onChangeCategoryBottonAction(ActionEvent event) {
        Main.getInstance().showScene("SelectCategory");
    }

    /**
     * ゲーム終了ボタンが押されたときの動作処理
     * @param event イベントハンドラ
     */
    @FXML
    void onExitBottonAction(ActionEvent event) {
        Main.getInstance().close(0);
    }
    
}
