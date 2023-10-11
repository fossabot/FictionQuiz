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

        startView();
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
     * スタート画面の初期描画
     */
    public void startView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Start.fxml"));
            BorderPane root = (BorderPane) fxmlLoader.load();
            Properties prop = gameValues.getProperties();

            Dimension windowDim = prop.getMainFrameDim();
            Scene scene = new Scene(root, windowDim.width, windowDim.getHeight());
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            primaryStage.setTitle(prop.getGameTitle());
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * クイズジャンル選択画面の初期描画
     */
    public void selectView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SelectCategory.fxml"));
            BorderPane root = (BorderPane) fxmlLoader.load();
            Properties prop = gameValues.getProperties();

            Dimension windowDim = prop.getMainFrameDim();
            Scene scene = new Scene(root, windowDim.width, windowDim.getHeight());
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * クイズ画面の初期描画
     */
    public void gameView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Game.fxml"));
            BorderPane root = (BorderPane) fxmlLoader.load();
            Properties prop = gameValues.getProperties();

            Dimension windowDim = prop.getMainFrameDim();
            Scene scene = new Scene(root, windowDim.width, windowDim.getHeight());
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // アプリを終了するときの動作
    public void close() {
        Output.printlnAsInfo("ゲームを終了します。");

        db.disconnectDB();
        primaryStage.close();
        System.exit(0);
    }

}
