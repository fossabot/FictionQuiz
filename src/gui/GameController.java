package gui;

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

import io.Output;

/**
 * クイズの画面表示を管理するクラス
 */
public class GameController implements Initializable {

    /** クイズゲームの管理データを格納する変数 */
    private QuizGame quizGame;

    ////////////// クイズの出題画面 //////////////
    /** 出題した回数（第X問）を表示するラベル */
    @FXML
    private Label counterLabel;

    /** 問題文を表示するラベル */
    @FXML
    private Label questionLabel;

    /** 選択肢ボタン */
    @FXML
    private Button selectButton1;

    @FXML
    private Button selectButton2;

    @FXML
    private Button selectButton3;

    @FXML
    private Button selectButton4;

    ////////////// クイズの判定画面 //////////////

    /** クイズの判定画面 */
    @FXML
    private VBox judgePane;

    /** 回答の正否を表示するラベル */
    @FXML
    private Label judgeLabel;

    /** 問題の解説を表示するラベル */
    @FXML
    private Label commentLabel;

    /** 次の問題に遷移するボタン */
    @FXML
    private Button nextButton;

    ////////////// クイズ終了後の結果画面 //////////////
    /** 結果を表示する画面 */
    @FXML
    private VBox resultPane;

    /** クイズ結果を表示するラベル */
    @FXML
    private Label resultLabel;

    /** クイズ再挑戦ボタン */
    @FXML
    private Button repeatButton;

    /** ジャンル変更ボタン */
    @FXML
    private Button changeCategoryButton;

    /** ゲーム終了ボタン */
    @FXML
    private Button exitButton;

    /**
     * ゲーム画面の描画初期化とクイズの生成を行う
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        commentLabel.setPrefWidth(Main.getInstance().getPrimaryStage().getWidth() - 100);

        // クイズの生成
        GameValues gameValues = Main.getInstance().getGameValues();
        QuizGame quizGame = new QuizGame();
        this.quizGame = quizGame;

        quizGame.generateFictionList(Main.getInstance().getDB(), Main.getInstance().getCategoryId());

        quizGame.generateQuiz(gameValues.getProperties());
        if (quizGame.nextQuiz()) {
            updatePane(quizGame.getCurrentQuestion());
        } else {
            throw new NullPointerException("クイズが作成されませんでした。");
        }
    }

    /**
     * クイズゲーム管理データを返す
     * @return クイズゲーム管理データ
     */
    public QuizGame getQuizGame() {
        return quizGame;
    }

    /**
     * クイズの回答を判定し、判定結果画面を表示する
     * @param selectNum 回答した選択肢の番号
     */
    private void judge(int selectNum) {
        if (quizGame.judgeQuiz(selectNum)) {
            judgeLabel.setText("正解");
            judgeLabel.setTextFill(Paint.valueOf("GREEN"));
        } else {
            judgeLabel.setText("不正解");
            judgeLabel.setTextFill(Paint.valueOf("RED"));
        }

        Question question = quizGame.getCurrentQuestion();
        commentLabel.setText(String.format("解説：\n「%s」の作者は「%s」です。\n\n", question.getQuestion(), question.getAnswer()));

        judgePane.setVisible(true);
    }

    /**
     * ゲーム画面の表示内容を更新する
     * @param question
     */
    private void updatePane(Question question) {
        try {
            counterLabel.setText("第 " + quizGame.getCurrentQuestionNum() + " 問");
            questionLabel.setText(question.getQuestion());
            selectButton1.setText(question.getSelectString(0));
            selectButton2.setText(question.getSelectString(1));
            selectButton3.setText(question.getSelectString(2));
            selectButton4.setText(question.getSelectString(3));
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
        this.judge(1);

    }

    /**
     * 選択肢２のボタンが押されたときの動作処理
     * @param event イベントハンドラ
     */
    @FXML
    void onSelectBotton2Action(ActionEvent event) {

        this.judge(2);
    }

    /**
     * 選択肢３のボタンが押されたときの動作処理
     * @param event イベントハンドラ
     */
    @FXML
    void onSelectBotton3Action(ActionEvent event) {

        this.judge(3);
    }

    /**
     * 選択肢４のボタンが押されたときの動作処理
     * @param event イベントハンドラ
     */
    @FXML
    void onSelectBotton4Action(ActionEvent event) {

        this.judge(4);
    }

    /**
     * 次の問題に進むボタンが押されたときの動作処理
     * @param event イベントハンドラ
     */
    @FXML
    void onNextBottonAction(ActionEvent event) {
        if (quizGame.nextQuiz()) {
            //次の問題を表示
            updatePane(quizGame.getCurrentQuestion());
            judgePane.setVisible(false);
        } else {
            // クイズ終了
            Output.printlnAsInfo("クイズが空になりました");

            resultLabel.setText(String.format(
                    "正解数は %d 問中、 %d 問です。", quizGame.getCurrentQuestionNum(), quizGame.getCurrentAnswerNum()));
            resultPane.setVisible(true);
        }
    }

    /**
     * 再挑戦ボタンが押されたときの動作処理
     * @param event イベントハンドラ
     */
    @FXML
    void onRepeatButtonAction(ActionEvent event) {
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
        Main.getInstance().close();
    }

}
