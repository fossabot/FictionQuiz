package cui;

import java.util.List;
import java.util.Scanner;

import common.CommonMethods;
import common.ConstantValue;
import quiz.Code;
import quiz.ControlCodeList;
import quiz.Question;
import quiz.QuizGame;
import io.Output;
import io.Properties;

/**
 * コマンドライン版の「作品クイズ」のゲーム全体を処理するクラス
 * @author komoto
 * @version 1.0
 *
 */
public class CuiMain {

    /** プロパティファイルの読込み結果を格納する変数 */
    private static Properties prop;

    /** クイズゲームの管理データを格納する変数 */
    private static QuizGame quizGame;

    /** ジャンルのコード表を格納する変数 */
    private static List<Code> categoryCodes;

    /** 標準入力のスキャナーを格納する変数 */
    private static Scanner scanner;

    /**
     * 「作品クイズ」のゲーム全体の処理を行う。
     * @param args プログラム実行時の引数
     */
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        prop = new Properties(ConstantValue.RES_DIR_PATH, ConstantValue.PROPERTIES_FILE);

        System.out.println(prop.getGameTitle());
        System.out.println("");

        quizGame = new QuizGame(ConstantValue.RES_DIR_PATH + "/" + prop.getDatabaseFile());
        categoryCodes = quizGame.getCategoryCodes();
        quizGame.generateSakuhinList(setCategoryOfQuiz());

        while (true) {
            int qNum = prepareQuiz();
            runQuiz(qNum);

            System.out.println("同じジャンルの問題に再挑戦しますか？（y：はい / n：いいえ）：");
            Output.printAskWithBoolen("再挑戦？");
            if (scanner.next().equals("y")) {
                continue; // y を入力したときはクイズ内容を変えて再実行
            } else {
                break; // y 以外を入力したときはゲーム終了
            }
        }

        System.out.println("ゲームを終了します。");
        scanner.close();
    }

    /**
     * 出題するクイズのジャンルを設定する
     * @return 選択したジャンルのカテゴリーコード
     */
    private static int setCategoryOfQuiz() {
        int inputNum; // 標準入力を数値型に変換した値
        int categoryId;

        while (true) {
            System.out.println("出題されるクイズのジャンルを選択してください。");
            for (int i = 0; i < categoryCodes.size(); i++) {
                Output.printlnSelectEntry(i + 1, categoryCodes.get(i).getEntry());
            }
            Output.printAskWithNum("選択肢？", 1, categoryCodes.size());

            inputNum = CommonMethods.parseIntWithRange(scanner.next(), 1, categoryCodes.size());
            if (inputNum < 1) {
                // 不適切な値を入力した場合は再度ジャンル選択を行う。
                Output.printlnAsWarning("改めて選択肢を入力してください。");
                System.out.println("");
                continue;
            } else {
                // 適切な値を入力した場合は次の処理に進む。
                break;
            }
        }

        categoryId = categoryCodes.get(inputNum - 1).getId();
        Output.printlnAsInfo(
                "クイズジャンルとして「" +  ControlCodeList.getEntryFromCode(categoryCodes, categoryId) + "」が選択されました。");
        

        return categoryId;
    }

    /**
     * クイズ出題の前処理を行う。
     * @return 出題する問題数
     */
    private static int prepareQuiz() {
        int questionNum = 0; // 出題する問題数

        Output.printlnAsInfo("クイズを準備します。");
        quizGame.generateQuiz(prop.getQuestionNum(), prop.getSelectNum());

        // 出題する問題数を設定する
        // 原則はプロパティファイルの値を設定するが、
        // 作成された問題が設定された問題数よりも少ない場合は、作成された問題数を出題数として設定する。
        if (quizGame.getQuizQueuSize() < prop.getQuestionNum()) {
            questionNum = quizGame.getQuizQueuSize();
            Output.printlnAsWarning("データベース件数が少ないため、出題数は変更されました。出題数： " + questionNum);
        } else {
            questionNum = prop.getQuestionNum();
        }

        Output.printlnAsInfo("クイズの準備が完了しました。出題数：" + questionNum);
        System.out.println("");

        return questionNum;
    }

    /**
     * クイズの出題、回答の判定、結果出力までを行う。
     * @param questionNum 出題される問題数
     */
    private static void runQuiz(int questionNum) {
        int inputNum; // 標準入力を数値型に変換した値

        for (int i = 0; i < questionNum; i++) {
            Question question = null;
            
            Output.printlnSeparateLine();
            quizGame.setQuiz();
            
            question = quizGame.getCurrentQuestion();
            
            // 出題
            while (true) {
                int selectNum = question.getSelectNum();
                
                System.out.printf("第 %d 問\n", quizGame.getCurrentQuestionNum());
                System.out.printf("「%s」の作者は誰でしょう？\n", question.getQuestion());
                
                for(int j = 0; j < selectNum; j++) {
                    System.out.printf("%2d. %s\n", j + 1, question.getSelectString(j));
                }
                
                System.out.println("");
                Output.printAskWithNum("回答？", 1, selectNum);

                inputNum = CommonMethods.parseIntWithRange(scanner.next(), 1, selectNum);
                if (inputNum < 1) {
                    // 不適切な値を入力した場合は再度ジャンル選択を行う。
                    Output.printlnAsWarning("改めて選択肢を入力してください。");
                    System.out.println("");
                    continue;
                } else {
                    break; // 適切な値を入力した場合は次の処理に進む。
                }
            }

            // 正答判定
            quizGame.judgeQuiz(inputNum);
            System.out.printf("解説：「%s」の作者は「%s」です。\n\n", question.getQuestion(), question.getAnswer());
        }

        // クイズの最終結果を表示する
        Output.printlnSeparateLine();
        System.out.printf("クイズの結果発表\n");
        System.out.printf("正解数は %d 問中、 %d 問です。\n\n", quizGame.getCurrentQuestionNum(), quizGame.getCurrentAnswerNum());
        Output.printlnSeparateLine();
        System.out.println("");
    }
}
