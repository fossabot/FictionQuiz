package io;

/**
 * 画面出力の操作に関するクラス
 * @author komoto
 *
 */
public class Output {
    
    /** 赤色の色コードを表す定数 */
    private static final String RED = "\u001b[00;31m";

    /** 緑色の色コードを表す定数 */
    private static final String GREEN = "\u001b[00;32m";

    /** 黄色の色コードを表す定数 */
    private static final String YELLOW = "\u001b[00;33m";

//    /** 紫色の色コードを表す定数 */
//    private static final String PURPLE = "\u001b[00;34m";
//
//    /** 桃色の色コードを表す定数 */
//    private static final String PINK   = "\u001b[00;35m";

    /** 青色の色コードを表す定数 */
    private static final String CYAN = "\u001b[00;36m";

    /** 色指定の終わりを表す定数 */
    private static final String END = "\u001b[00m";
    
    /**
     * エラーを表示する
     * @param str エラーの説明文
     */
    public static void printlnAsError(String str) {
        System.out.println(RED + "Error: " + str + END);
    }

    /**
     * 警告を表示する
     * @param str 警告の説明文
     */
    public static void printlnAsWarning(String str) {
        System.out.println(YELLOW + "Warning: " + str + END);
    }

    /**
     * 情報を表示する
     * @param str 情報の説明文
     */
    public static void printlnAsInfo(String str) {
        System.out.println(CYAN + "Info: " + str + END);
    }

    /**
     * 正解を表示する
     * @param str 正解の説明文（空の場合、説明を表示しない）
     */
    public static void printlnAsCollect(String str) {
        if (!str.isEmpty()) {
            str = "： " + str;
        }
        System.out.println(GREEN + "正解！" + str + END);
    }

    /**
     * 正解を表示する
     */
    public static void printlnAsCollect() {
        printlnAsCollect("");
    }

    /**
     * 不正解を表示する
     * @param str 不正解の説明文（空の場合、説明を表示しない）
     */
    public static void printlnAsIncollect(String str) {
        if (!str.isEmpty()) {
            str = "： " + str;
        }
        System.out.println(RED + "不正解" + str + END);
    }

    /**
     * 不正解を表示する
     */
    public static void printlnAsIncollect() {
        printlnAsIncollect("");
    }
    
    /**
     * 入力待ち時に入力候補付きで文字列を表示する。表示例：{@code 回答？[1-4]：}
     * @param str 入力待ちに表示する文字列
     * @param minNum 入力の最小値
     * @param maxNum 入力の最大値
     */
    public static void printAskWithNum(String str, int minNum, int maxNum) {
        System.out.printf("%s [%d-%d]：", str, minNum, maxNum);
    }
    
    /**
     * 入力待ち時に入力候補付きで文字列を表示する。表示例： {@code 回答？[y/n]：}
     * @param str 入力待ちに表示する文字列
     */
    public static void printAskWithBoolen(String str) {
        System.out.printf("%s [y/n]：", str);
    }
    
    /**
     * 区切り線を表示する。
     */
    public static void printlnSeparateLine() {
        System.out.println("--------------------------------------------------");
    }
    
    /**
     * 選択肢の要素を表示する。
     * @param num 選択肢の要素の番号
     * @param str 選択肢の要素の文字列
     */
    public static void printlnSelectEntry(int num, String str) {
        System.out.printf("%3d. %s\n", num, str);
    }
}
