package gui.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import quiz.GameValues;
import quiz.Question;
import quiz.QuizGame;

import java.net.URL;
import java.util.ResourceBundle;

import gui.Main;
import io.Output;

/**
 * クイズの画面表示を管理するクラス
 */
public class JudgeController implements Initializable {

    /** クイズゲームの管理データを格納する変数 */
    private QuizGame quizGame;

    ////////////// クイズの判定画面 //////////////

    /** 回答の正否を表示するラベル */
    @FXML
    private Label judgeLabel;

    /** 問題の解説を表示するラベル */
    @FXML
    private Label commentLabel;

    /** 次の問題に遷移するボタン */
    @FXML
    private Button nextButton;

    /**
     * ゲーム画面の描画初期化とクイズの生成を行う
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        commentLabel.setPrefWidth(Main.getInstance().getPrimaryStage().getWidth() - 100);
    }

    /**
     * クイズの回答を判定し、判定結果画面を表示する
     * @param quizGame クイズの情報を持つ変数
     * @param selectNum 回答した選択肢の番号
     */
    public void updatePane(QuizGame quizGame, int selectNum) {
        if (this.quizGame == null) {
            this.quizGame = quizGame;
        }

        if (quizGame.judgeQuiz(selectNum)) {
            judgeLabel.setText("正解");
            judgeLabel.setTextFill(Paint.valueOf("GREEN"));
        } else {
            judgeLabel.setText("不正解");
            judgeLabel.setTextFill(Paint.valueOf("RED"));
        }

        Question question = quizGame.getCurrentQuestion();
        commentLabel.setText(String.format("解説：\n「%s」の作者は「%s」です。",
                question.getQuestion(), question.getAnswer()));
    }

    /**
     * 次の問題に進むボタンが押されたときの動作処理
     * @param event イベントハンドラ
     */
    @FXML
    void onNextBottonAction(ActionEvent event) {
        if (quizGame.nextQuiz()) {
            //次の問題を表示
            Game.getGame().showQuizScene();
        } else {
            // クイズ終了
            Game.getGame().showResultView();
        }
    }

}
