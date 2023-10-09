package io;

/**
 * プログラム全体で共有する関数を定義するクラス
 * @author komoto
 *
 */
public class CommonMethods {

    /**
     * 範囲付きで文字列を数字に変換する
     * 文字列 {@code str} を数字に変換し、その数字が条件範囲（ {@code minNum} 以上、かつ {@code maxNum} 以下）に含まれるか確認する。
     * @param str 数字に変換したい文字列
     * @param minNum 条件範囲の最小値
     * @param maxNum 条件範囲の最大値
     * @return 変換した数字が {@code minNum} 以上、かつ {@code maxNum} 以下の範囲に含まれる場合はその数字を返す。それ以外の場合は {@code minNum-1} の値を返す。
     */
    public static int parseIntWithRange(String str, int minNum, int maxNum) {
        try {
            int n = Integer.parseInt(str);

            if (n >= minNum && n <= maxNum) {
                return n; // 選択肢の範囲内
            } else {
                Output.printlnAsWarning("範囲外の数字が入力されました。");
                return minNum - 1; // 選択肢の範囲外
            }
        } catch (NumberFormatException e) {
            Output.printlnAsWarning("数字以外の文字が入力されました。");
            return minNum - 1; // 数字以外の文字
        }
    }
}
