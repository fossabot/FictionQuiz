package io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SQLite3のデータベースを管理・操作するクラス
 * SQLite3のデータベースに対して、接続・SQL文の発行・切断を実行する。
 * @author komoto
 *
 */
public class DatabaseSqlite3 {
    
    /** データベースの接続情報を格納する変数 */
    private Connection conn = null;
    
    /** SQL実行結果を格納する変数 */
    private Statement  stmt = null;
    
    /**
     * データベースに接続する
     * @param dbFileString データベースファイルへのパス
     */
    public DatabaseSqlite3(String dbFileString) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbFileString);
            stmt = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Output.printlnAsInfo("データベースに接続しました。");
    }
    
    /**
     * データベースから切断する
     */
    public void disconnectDB() {
        try {
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Output.printlnAsInfo("データベースを切断しました。");
    }
    
    /**
     * SQL文を実行し、結果を返す
     * @param sql 実行するSQLの文字列
     * @return SQLの実行結果
     */
    public ResultSet executeQuery(String sql) {
        ResultSet rs = null;
        
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rs;
    }
}
