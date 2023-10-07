package quiz;

import java.util.List;

/**
 * {@code Code} 型リストを操作するクラス
 * @author komoto
 *
 */
public class ControlCodeList {

    /**
    * コード番号に対応する要素の文字列を返す。
    * @param codeList コード {@code Code} 型のリスト
    * @param code 検索するコード番号
    * @return 該当する要素の文字列
    */
    public static String getEntryFromCode(List<Code> codeList, int code) {
        for (int i = 0; i < codeList.size(); i++) {
            if (codeList.get(i).getId() == code) {
                return codeList.get(i).getEntry();
            }
        }
        return null;
    }

    /**
     * 要素 {@code entry} に対応するコード番号を返す。
     * @param codeList コード {@code Code} 型のリスト
     * @param entry 検索する要素の文字列
     * @return 該当するコード番号
     */
    public static int getCodeFromEntry(List<Code> codeList, String entry) {
        for (int i = 0; i < codeList.size(); i++) {
            if (codeList.get(i).getEntry().equals(entry)) {
                return codeList.get(i).getId();
            }
        }
        return -1;
    }

    /**
     * コード番号がリストに含まれているか
     * @param codeList コード {@code Code} 型のリスト
     * @param code 検索するコード番号
     * @return リストに存在する場合は {@code true} 、リストに存在しない場合は {@code false} を返す。
     */
    public static Boolean containsByCode(List<Code> codeList, int code) {
        for (int i = 0; i < codeList.size(); i++) {
            if (codeList.get(i).getId() == code) {
                return true;
            }
        }

        return false;
    }

    /**
     * 要素 {@code entry} がリストに含まれているか
     * @param codeList コード {@code Code} 型のリスト
     * @param entry 検索する要素の文字列
     * @return リストに存在する場合は {@code true} 、リストに存在しない場合は {@code false} を返す。
     */
    public static Boolean containsByEntry(List<Code> codeList, String entry) {
        for (int i = 0; i < codeList.size(); i++) {
            if (codeList.get(i).getEntry().equals(entry)) {
                return true;
            }
        }

        return false;
    }
}
