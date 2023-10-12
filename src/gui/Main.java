package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

import java.awt.Dimension;

import quiz.GameValues;
import io.DatabaseSqlite3;
import io.Output;
import io.Properties;

public class Main extends Application {
    public static Main instance;
    private GameValues gameValues;
    private Stage primaryStage;

    private DatabaseSqlite3 db;

    private int categoryId; //選択したカテゴリーのID番号

    public Main() {
        instance = this;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // ウィンドウを閉じたときの動作を設定
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            this.close();
        });

        gameValues = new GameValues();
        gameValues.setProperties();
        Properties prop = gameValues.getProperties();
        db = new DatabaseSqlite3(gameValues.getResDirPath() + "/" + prop.getDatabaseFile());

        primaryStage.setTitle(prop.getGameTitle());
        showScene("Start");
    }

    public static Main getInstance() {
        return instance;
    }

    public GameValues getGameValues() {
        return gameValues;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public DatabaseSqlite3 getDB() {
        return db;
    }

    public int getCategoryId() {
        return categoryId;
    }

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
            eventString = "start\\" + eventString;
            break;
        }
        case "SelectCategory": {
            eventString = "category\\" + eventString;
            break;
        }
        case "Game": {
            eventString = "game\\" + eventString;
            break;
        }
        default:
            throw new IllegalArgumentException("Unexpected value: " + eventString);
        }

        // 画面描画処理を行う
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(eventString + ".fxml"));
            BorderPane root = (BorderPane) fxmlLoader.load();
            Properties prop = gameValues.getProperties();

            Dimension windowDim = prop.getMainFrameDim();
            Scene scene = new Scene(root, windowDim.width, windowDim.getHeight());
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            this.close();
        }
    }

    /**
     * アプリを閉じる時の終了処理
     */
    public void close() {
        Output.printlnAsInfo("ゲームを終了します。");

        db.disconnectDB();
        primaryStage.close();
        System.exit(0);
    }

}
