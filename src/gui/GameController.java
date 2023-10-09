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

    @FXML
    private Button selectButton1;

    @FXML
    private Button selectButton2;

    @FXML
    private Button selectButton3;

    @FXML
    private Button selectButton4;

    ////////////// クイズの判定画面 //////////////
    @FXML
    private VBox judgePane;

    @FXML
    private Label judgeLabel;

    @FXML
    private Label commentLabel;

    @FXML
    private Button nextButton;

    ////////////// クイズ終了後の結果画面 //////////////
    @FXML
    private VBox resultPane;

    @FXML
    private Label resultLabel;

    @FXML
    private Button repeatButton;

    @FXML
    private Button changeCategoryButton;

    @FXML
    private Button exitButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GameValues gameValues = Main.getInstance().getGameValues();
        
        commentLabel.setPrefWidth(Main.getInstance().getPrimaryStage().getWidth() - 100);

        QuizGame quizGame = new QuizGame();
        this.quizGame = quizGame;

        quizGame.generateSakuhinList(Main.getInstance().getDB(), 2);

        quizGame.generateQuiz(gameValues.getProperties());
        if (quizGame.nextQuiz()) {
            updatePane(quizGame.getCurrentQuestion());
        } else {
            throw new NullPointerException("クイズが作成されませんでした。");
        }
    }

    // クイズゲーム管理データを返す
    public QuizGame getQuizGame() {
        return quizGame;
    }

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

    // ゲームペイン画面の表示を更新する
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

    @FXML
    void onSelectBotton1Action(ActionEvent event) {
        this.judge(1);

    }

    @FXML
    void onSelectBotton2Action(ActionEvent event) {

        this.judge(2);
    }

    @FXML
    void onSelectBotton3Action(ActionEvent event) {

        this.judge(3);
    }

    @FXML
    void onSelectBotton4Action(ActionEvent event) {

        this.judge(4);
    }

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

    @FXML
    void onRepeatButtonAction(ActionEvent event) {

    }

    @FXML
    void onChangeCategoryBottonAction(ActionEvent event) {

    }

    @FXML
    void onExitBottonAction(ActionEvent event) {
        Main.getInstance().close();
    }

}
