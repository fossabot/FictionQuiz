package gui.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import quiz.GameValues;
import quiz.Question;
import quiz.QuizGame;

import java.util.List;
import gui.Main;

/**
 * クイズの画面表示を管理するクラス
 */
public class QuizController {

    /** 出題した回数（第X問）を表示するラベル */
    @FXML
    private Label counterLabel;

    /** 問題文を表示するラベル */
    @FXML
    private Label questionLabel;

    /** 選択肢ボタン */
    @FXML
    private List<Button> selectButtonList;

    /**
     * ゲーム生成を行う
     * @return クイズゲーム管理データ
     */
    QuizGame generateQuiz() {
        QuizGame quizGame = new QuizGame();
        GameValues gameValues = Main.getInstance().getGameValues();

        quizGame.generateFictionList(Main.getInstance().getDB(), Main.getInstance().getCategoryId());

        quizGame.generateQuiz(gameValues.getProperties());
        if (quizGame.nextQuiz() == false) {
            throw new NullPointerException("クイズが作成されませんでした。");
        }
        
        return quizGame;
    }

    /**
     * クイズ画面の表示内容を更新する
     */
    void updatePane(QuizGame quizGame) {
        Question question = quizGame.getCurrentQuestion();
        
        try {
            counterLabel.setText("第 " + quizGame.getCurrentQuestionNum() + " 問");
            questionLabel.setText(question.getQuestion());
            // 選択肢ボタンの表示テキストを更新
            for (int i = 0; i < selectButtonList.size(); i++) {
                selectButtonList.get(i).setText(question.getSelectString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 選択肢１のボタンが押されたときの動作処理
     * @param event イベントハンドラ
     */
    @FXML
    void onSelectBotton1Action(ActionEvent event) {
        Question question = Game.getInstance().getQuizGame().getCurrentQuestion();
        Game.getInstance().showJudgeView(question.getSelectString(0));
    }

    /**
     * 選択肢２のボタンが押されたときの動作処理
     * @param event イベントハンドラ
     */
    @FXML
    void onSelectBotton2Action(ActionEvent event) {
        Question question = Game.getInstance().getQuizGame().getCurrentQuestion();
        Game.getInstance().showJudgeView(question.getSelectString(1));
    }

    /**
     * 選択肢３のボタンが押されたときの動作処理
     * @param event イベントハンドラ
     */
    @FXML
    void onSelectBotton3Action(ActionEvent event) {
        Question question = Game.getInstance().getQuizGame().getCurrentQuestion();
        Game.getInstance().showJudgeView(question.getSelectString(2));
    }

    /**
     * 選択肢４のボタンが押されたときの動作処理
     * @param event イベントハンドラ
     */
    @FXML
    void onSelectBotton4Action(ActionEvent event) {
        Question question = Game.getInstance().getQuizGame().getCurrentQuestion();
        Game.getInstance().showJudgeView(question.getSelectString(3));
    }

}
