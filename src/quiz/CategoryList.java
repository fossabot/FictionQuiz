package quiz;

import java.sql.ResultSet;
import java.sql.SQLException;

import io.DatabaseSqlite3;

/**
 * カテゴリーリストのクラス
 * @author komoto
 *
 */
public class CategoryList extends CodeList {
    
    /**
    * カテゴリーリストを作成して返す
    * @param db データベースの接続情報
    */
    public CategoryList(DatabaseSqlite3 db) {
        super();

        ResultSet rs = db.executeQuery("SELECT * FROM CategoryTable");

        try {
            while (rs.next()) {
                this.add(Integer.parseInt(rs.getString("id")), rs.getString("category"));
            }
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }
    }

}
