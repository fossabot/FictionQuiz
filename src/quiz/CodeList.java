package quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code Code} 型のリストを管理・操作するクラス
 * @author komoto
 *
 */
public class CodeList {
    
    /** コードのリストを格納する変数 */
    private final List<Code> codeList;

    /**
     * コードリストを作成する
     */
    public CodeList() {
        codeList = new ArrayList<Code>();
    }
    
    /**
     * コードリストが空か判定する
     * @return リストが空の場合は {@code true}、空でない場合は {@code false} を返す。
     */
    public Boolean isEmpty() {
        return codeList.isEmpty();
    }
    
    /**
     * コードリストが持つ要素の個数を返す
     * @return コードリストが持つ要素の個数
     */
    public int size() {
        return codeList.size();
    }
    
    public int getCode(int i) {
        return codeList.get(i).getId();
    }

    public String getEntry(int i) {
        return codeList.get(i).getEntry();
    }
    
    /**
     * コードリストに要素を追加する
     * @param id コード番号
     * @param entry コード番号に対応する値
     */
    public void add(int id, String entry) {
        Code code = new Code(id, entry);
        codeList.add(code);
    }
    
    /**
     * コードリストの中身を消去する
     */
    public void clear() {
        codeList.clear();
    }
    
    /**
     * コードリストの中身の順序をランダムに入れ替える
     */
    public void shuffle() {
        Collections.shuffle(codeList);
    }
    
    /**
    * コード番号に対応する要素の文字列を返す。
    * @param code 検索するコード番号
    * @return 該当する要素の文字列
    */
    public String getEntryFromCode(int code) {
        for (int i = 0; i < codeList.size(); i++) {
            if (codeList.get(i).getId() == code) {
                return codeList.get(i).getEntry();
            }
        }
        return null;
    }

    /**
     * 要素 {@code entry} に対応するコード番号を返す。
     * @param entry 検索する要素の文字列
     * @return 該当するコード番号
     */
    public int getCodeFromEntry(String entry) {
        for (int i = 0; i < codeList.size(); i++) {
            if (codeList.get(i).getEntry().equals(entry)) {
                return codeList.get(i).getId();
            }
        }
        return -1;
    }

    /**
     * コード番号がリストに含まれているか
     * @param code 検索するコード番号
     * @return リストに存在する場合は {@code true} 、リストに存在しない場合は {@code false} を返す。
     */
    public Boolean containsByCode(int code) {
        for (int i = 0; i < codeList.size(); i++) {
            if (codeList.get(i).getId() == code) {
                return true;
            }
        }
        return false;
    }

    /**
     * 要素 {@code entry} がリストに含まれているか
     * @param entry 検索する要素の文字列
     * @return リストに存在する場合は {@code true} 、リストに存在しない場合は {@code false} を返す。
     */
    public Boolean containsByEntry(String entry) {
        for (int i = 0; i < codeList.size(); i++) {
            if (codeList.get(i).getEntry().equals(entry)) {
                return true;
            }
        }

        return false;
    }
}
