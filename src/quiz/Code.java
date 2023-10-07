package quiz;

/**
 * コード表クラス
 * コード番号と、それに対応する文字列を紐付ける。
 * @author komoto
 *
 */
public class Code {
    
    /** コード番号を表す変数 */
    private int id;

    /** コードに対応する文字列を表す変数 */
    private String entry;

    /**
     * コード番号と文字列を設定する
     * @param id コード番号
     * @param entry コードに対応する要素
     */
    public Code(int id, String entry) {
        this.id    = id;
        this.entry = entry;
    }

    /**
     * コード番号を返す
     * @return コード番号 {@code id}
     */
    public int getId() {
        return id;
    }

    /**
     * エントリーの値を返す
     * @return エントリー {@code entry}
     */
    public String getEntry() {
        return entry;
    }
}
