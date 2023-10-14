package gui.game;

import java.awt.Dimension;

import gui.Main;
import io.Properties;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import quiz.GameValues;
import quiz.QuizGame;

/**
 * クイズの流れを管理するクラス
 */
public class Game {
    
    /** 自分自身を表すインスタンス */
    private static Game instance = null;

    /** ゲーム全体で使用するインスタンス */
    private final GameValues gameValues;
    
    /** ゲームを表示するウィンドウ */
    private final Stage primaryStage;

    /** クイズゲームの管理データを格納する変数 */
    private QuizGame quizGame;

    /** クイズ出題画面のシーンを表すインスタンス */
    private SceneInfo quizSceneInfo = null;
    
    /** 判定画面のシーンを表すインスタンス */
    private SceneInfo judgeSceneInfo = null;
    
    /** 結果画面のシーンを表すインスタンス */
    private SceneInfo resultSceneInfo = null;

    /**
     * シーンを表示する上で必要なインスタンスをまとめて保持する内部クラス
     */
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

    /**
     * クイズを出題する前処理
     * @param primaryStage ゲームを表示するウィンドウ
     * @param gameValues ゲーム全体で使用するインスタンス
     */
    public Game(Stage primaryStage, GameValues gameValues) {
        Game.instance = this;
        this.primaryStage = primaryStage;
        this.gameValues = gameValues;
    }
    
    /**
     * Game自身のインスタンスを返す
     * @return Gameのインスタンス
     */
    static Game getInstance() {
        return instance;
    }

    /**
     * QuizGameのインスタンスを返す
     * @return QuizGameのインスタンス
     */
    public QuizGame getQuizGame() {
        return quizGame;
    }

    /**
     * クイズ出題画面の描画を行う
     */
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

    /**
     * 判定画面の描画を行う
     * @param slectString 選択した回答の文字列
     */
    void showJudgeView(String slectString) {
        if (judgeSceneInfo == null) {
            judgeSceneInfo = generateScene("Judge");
        }

        // 回答の正否を判定し、判定画面を描写する。
        JudgeController judgeController = (JudgeController) judgeSceneInfo.getFxmlLoader().getController();
        judgeController.updatePane(quizGame, slectString);
        setPaneVisible("Judge");
        primaryStage.setScene(judgeSceneInfo.getScene());
        primaryStage.show();
    }

    /**
     * 結果画面の描画を行う
     */
    void showResultView() {
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
