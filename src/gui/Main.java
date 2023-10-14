package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

import java.awt.Dimension;

import gui.game.Game;
import quiz.GameValues;
import io.DatabaseSqlite3;
import io.Output;
import io.Properties;

public class Main extends Application {
    
    /** 自分自身を表すインスタンス */
    public static Main instance;

    /** ゲーム全体で使用するインスタンス */
    private GameValues gameValues;
    
    /** ゲームを表示するウィンドウ */
    private Stage primaryStage;

    /** データベースのインスタンス */
    private DatabaseSqlite3 db;

    /** ジャンル選択画面で選択したジャンルのID */
    private int categoryId;

    /**
     * JavaFXによる画面描画を開始する
     * @param args プログラム実行時の引数
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * 初期化とデータベース接続を行い、最初の画面を表示する
     * @param primaryStage ゲームを表示するウィンドウ
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Main.instance = this;

        // ウィンドウを閉じたときの動作を設定
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            this.close(0);
        });

        gameValues = new GameValues();
        gameValues.setProperties();
        Properties prop = gameValues.getProperties();
        db = new DatabaseSqlite3(gameValues.getResDirPath() + "/" + prop.getDatabaseFile());

        primaryStage.setTitle(prop.getGameTitle());
        showScene("Start");
    }

    /**
     * Main自身のインスタンスを返す
     * @return Mainのインスタンス
     */
    public static Main getInstance() {
        return instance;
    }
    
    /**
     * ゲーム全体で使用するインスタンスを返す
     * @return ゲーム全体で使用するインスタンス
     */
    public GameValues getGameValues() {
        return gameValues;
    }

    /**
     * ゲームを表示するウィンドウを表すインスタンスを返す
     * @return ゲームを表示するウィンドウを表すインスタンス
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * データベースのインスタンスを返す
     * @return データベースのインスタンス
     */
    public DatabaseSqlite3 getDB() {
        return db;
    }

    /**
     * 現在選択されているジャンルのIDを返す
     * @return 現在選択されているジャンルのID
     */
    public int getCategoryId() {
        return categoryId;
    }
    
    /**
     * ジャンルのIDを設定する
     * @return ジャンルのIDを設定する
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 画面描画の処理
     * @param eventString 表示するイベント名（イベント名はFXMLファイル名に紐づく）
     * - "Start"          : スタート画面
     * - "SelectCategory" : ジャンル選択画面
     * - "Game"           : ゲーム画面
     */
    public void showScene(String eventString) {
        // 表示するイベント名の格納されているディレクトリ名を付与する
        switch (eventString) {
        case "Start": {
            eventString = "start\\" + eventString + ".fxml";
            break;
        }
        case "SelectCategory": {
            eventString = "category\\" + eventString + ".fxml";
            break;
        }
        case "Game": {
            Game game = new Game(primaryStage, gameValues);
            game.showQuizScene();
            return;
        }
        default:
            throw new IllegalArgumentException("Unexpected value: " + eventString);
        }

        // 画面描画処理を行う
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(eventString));
            BorderPane root = (BorderPane) fxmlLoader.load();
            Properties prop = gameValues.getProperties();

            Dimension windowDim = prop.getMainFrameDim();
            Scene scene = new Scene(root, windowDim.width, windowDim.getHeight());
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            this.close(1);
        }
    }

    /**
     * アプリを閉じる時の終了処理
     * @param code エラーコード（正常時は0、異常時は1）
     */
    public void close(int code) {
        Output.printlnAsInfo("ゲームを終了します。");

        db.disconnectDB();
        primaryStage.close();
        System.exit(code);
    }

}
