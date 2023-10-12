package gui.game;

import java.awt.Dimension;

import gui.Main;
import io.Properties;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import quiz.GameValues;
import quiz.Question;
import quiz.QuizGame;

/**
 * クイズの流れを管理するクラス
 */
public class Game {
    private static Game instance = null;

    private final Stage primaryStage;
    private final GameValues gameValues;

    /** クイズゲームの管理データを格納する変数 */
    private QuizGame quizGame;

    private SceneInfo quizSceneInfo = null;
    private SceneInfo judgeSceneInfo = null;
    private SceneInfo resultSceneInfo = null;

    class SceneInfo {
        private final FXMLLoader fxmlLoader;
        private final BorderPane rootPane;
        private final Scene scene;

        public SceneInfo(FXMLLoader fxmlLoader, BorderPane rootPane, Scene scene) {
            this.fxmlLoader = fxmlLoader;
            this.rootPane = rootPane;
            this.scene = scene;
        }

        public FXMLLoader getFxmlLoader() {
            return fxmlLoader;
        }

        public BorderPane getRootPane() {
            return rootPane;
        }

        public Scene getScene() {
            return scene;
        }
    }

    // クイズゲームの開始（準備）
    public Game(Stage primaryStage, GameValues gameValues) {
        Game.instance = this;
        this.primaryStage = primaryStage;
        this.gameValues = gameValues;
    }

    static Game getGame() {
        return instance;
    }

    public QuizGame getQuizGame() {
        return quizGame;
    }

    // クイズ画面を表示
    public void showQuizScene() {
        if (quizSceneInfo == null) {
            quizSceneInfo = generateScene("Quiz");
        }

        QuizController quizController = (QuizController) quizSceneInfo.getFxmlLoader().getController();
        if (this.quizGame == null) {
            this.quizGame = quizController.generateQuiz();
        }

        // 判定画面を描写する。
        quizController.updatePane(quizGame);
        setPaneVisible("Quiz");
        primaryStage.setScene(quizSceneInfo.getScene());
        primaryStage.show();
    }

    // 判定画面を表示
    public void showJudgeView(int i) {
        if (judgeSceneInfo == null) {
            judgeSceneInfo = generateScene("Judge");
        }

        // 回答の正否を判定し、判定画面を描写する。
        JudgeController judgeController = (JudgeController) judgeSceneInfo.getFxmlLoader().getController();
        judgeController.updatePane(quizGame, i);
        setPaneVisible("Judge");
        primaryStage.setScene(judgeSceneInfo.getScene());      
        primaryStage.show();
    }

    // 結果画面を表示
    public void showResultView() {
        if (resultSceneInfo == null) {
            resultSceneInfo = generateScene("Result");
        }

        // 結果画面を描写する。
        ResultController resultController = (ResultController) resultSceneInfo.getFxmlLoader().getController();
        resultController.updatePane(quizGame);
        setPaneVisible("Result");
        primaryStage.setScene(resultSceneInfo.getScene());
        primaryStage.show();
    }

    /**
     * 画面描画処理を行う
     * @param eventString 表示するイベント名（イベント名はFXMLファイル名に紐づく）
     * - "Quiz"   : クイズ出題画面
     * - "Judge"  : 回答の判定画面
     * - "Result" : クイズ結果画面
     * @return 生成したFXMLLoaderの値
     */
    private SceneInfo generateScene(String eventString) {
        FXMLLoader fxmlLoader = null;
        BorderPane root = null;
        Scene scene = null;
        Properties prop = gameValues.getProperties();

        //        // 表示するイベント名の格納されているディレクトリ名を付与する
        //        eventString = "gui\\game\\" + eventString;

        try {
            String string = eventString + ".fxml";
            fxmlLoader = new FXMLLoader(getClass().getResource(string));
            root = (BorderPane) fxmlLoader.load();

            Dimension windowDim = prop.getMainFrameDim();
            scene = new Scene(root, windowDim.width, windowDim.getHeight());
            scene.getStylesheets().add(getClass().getResource("..\\application.css").toExternalForm());

        } catch (Exception e) {
            e.printStackTrace();
            Main.getInstance().close(1);
        }

        return new SceneInfo(fxmlLoader, root, scene);
    }

    /**
     * ペインの表示・非表示を切り替える
     * @param str
     * - "Quiz"   : クイズ出題画面
     * - "Judge"  : 回答の判定画面
     * - "Result" : クイズ結果画面
     */
    private void setPaneVisible(String str) {
        Boolean isQuiz = false;
        Boolean isJudge = false;
        Boolean isResult = false;
        
        switch (str) {
        case "Quiz": {
            isQuiz = true;
            break;
        }
        case "Judge": {
            isJudge = true;
            break;
        }
        case "Result": {
            isResult = true;
            break;
        }
        default:
            throw new IllegalArgumentException("Unexpected value: " + str);
        }
        
        
        if (quizSceneInfo != null) {
            quizSceneInfo.getRootPane().setVisible(isQuiz);
        }
        if (judgeSceneInfo != null) {
            judgeSceneInfo.getRootPane().setVisible(isJudge);
        }
        if (resultSceneInfo != null) {
            resultSceneInfo.getRootPane().setVisible(isResult);
        }
    }

}
