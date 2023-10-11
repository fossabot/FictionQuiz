package io;

import java.awt.Dimension;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * プロパティファイルを扱うクラス
 * @author komoto
 *
 */
public class Properties {

    /* プロパティファイルから読み込んだ値を格納する変数 */
    /** プロパティファイルから読み込まれる、データベースのファイル名 */
    private final String databaseFile;

    /** プロパティファイルから読み込まれる、ゲームのタイトル */
    private final String gameTitle;

    /** プロパティファイルから読み込まれる、出題する問題の個数 */
    private final int questionNum;

    /** プロパティファイルから読み込まれる、選択肢の個数 */
    private final int selectNum;

    /** プロパティファイルから読み込まれる、メイン画面のサイズ */
    private final Dimension mainFrameDim;

    /**
     * プロパティファイルを読み込み、値を設定する
     * @param res_dir プロパティファイルが格納されているディレクトリのパス
     * @param name プロパティファイルの名前（ファイル名）
     */
    public Properties(String res_dir, String name) {
        // プロパティファイルを取得する
        ResourceBundle rb = null;
        try {
            URLClassLoader urlLoader = new URLClassLoader(new URL[] { new File(res_dir).toURI().toURL() });
            rb = ResourceBundle.getBundle(name, Locale.getDefault(), urlLoader);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // 読み込んだ値を変数に設定する
        databaseFile = rb.getString("database_file");
        gameTitle = rb.getString("game_title");
        questionNum = Integer.parseInt(rb.getString("question_num"));
        selectNum = Integer.parseInt(rb.getString("select_num"));
        
        mainFrameDim = new Dimension(
                Integer.parseInt(rb.getString("main_frame_width")),
                Integer.parseInt(rb.getString("main_frame_height")));
    }
    
    /**
     * データベースのファイル名を返す
     * @return データベースのファイル名の文字列
     */
    public String getDatabaseFile() {
        return databaseFile;
    }

    /**
     * プロパティファイルから読み込まれたゲームのタイトルを返す
     * @return ゲームタイトルの文字列
     */
    public String getGameTitle() {
        return gameTitle;
    }

    /**
     * プロパティファイルから読み込まれた出題する問題の個数
     * @return 出題する問題の個数
     */
    public int getQuestionNum() {
        return questionNum;
    }

    /**
     * プロパティファイルから読み込まれた選択肢の個数
     * @return 選択肢の個数
     */
    public int getSelectNum() {
        return selectNum;
    }
    

    /**
     * プロパティファイルから読み込まれたメイン画面のサイズ
     * @return メイン画面のサイズ
     */
    public Dimension getMainFrameDim() {
        return mainFrameDim;
    }
}
