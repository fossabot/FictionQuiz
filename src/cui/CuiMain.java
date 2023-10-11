package cui;

import java.util.Scanner;

import quiz.CategoryList;
import quiz.GameValues;
import quiz.Question;
import quiz.QuizGame;
import io.CommonMethods;
import io.DatabaseSqlite3;
import io.Output;
import io.Properties;

/**
 * コマンドライン版の「作品クイズ」のゲーム全体を処理するクラス
 * @author komoto
 * @version 1.0
 *
 */
public class CuiMain {
    private static GameValues gameValues;

    /** 標準入力のスキャナーを格納する変数 */
    private static Scanner scanner;

    /** クイズゲームの管理データを格納する変数 */
    private static QuizGame quizGame;

    /**
     * 「作品クイズ」のゲーム全体の処理を行う。
     * @param args プログラム実行時の引数
     */
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        gameValues = new GameValues();
        gameValues.setProperties();
        Properties prop = gameValues.getProperties();

        System.out.println(prop.getGameTitle());
        System.out.println("");

        DatabaseSqlite3 db = new DatabaseSqlite3(gameValues.getResDirPath() + "/" + prop.getDatabaseFile());
        CategoryList categoryList = new CategoryList(db);
        gameValues.setCategoryCodes(categoryList);

        quizGame = new QuizGame(); /////////////// while内部にすべきか？ ///////////////

        while (true) {
            int categoryId, questionNum;

            // クイズジャンルの選択
            categoryId = selectQuizCategory();

            // 問題の生成
            quizGame.generateFictionList(db, categoryId);
            questionNum = quizGame.generateQuiz(prop);

            // クイズの実行
            for (int i = 0; i < questionNum; i++) {
                Output.printlnSeparateLine();
                nextQuiz();
            }

            // クイズの最終結果を表示する
            Output.printlnSeparateLine();
            System.out.printf("クイズの結果発表\n");
            System.out.printf("正解数は %d 問中、 %d 問です。\n\n", quizGame.getCurrentQuestionNum(),
                    quizGame.getCurrentAnswerNum());
            Output.printlnSeparateLine();
            System.out.println("");

            // 再挑戦するかを確認する
            System.out.println("再挑戦しますか？（y：はい / n：いいえ）：");
            Output.printAskWithBoolen("再挑戦？");

            if (scanner.next().equals("y")) {
                continue; // y を入力したときはクイズ内容を変えて再実行
            } else {
                break; // y 以外を入力したときはゲーム終了
            }
        }

        System.out.println("ゲームを終了します。");
        db.disconnectDB();
        scanner.close();
    }

    /**
     * 出題するクイズのジャンルを設定する
     * @return 選択したジャンルのカテゴリーコード
     */
    private static int selectQuizCategory() {
        int inputNum; // 標準入力を数値型に変換した値
        CategoryList categoryCodes = gameValues.getCategoryCodes();

        while (true) {
            System.out.println("出題されるクイズのジャンルを選択してください。");
            for (int i = 0; i < categoryCodes.size(); i++) {
                Output.printlnSelectEntry(i + 1, categoryCodes.getEntry(i));
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

        int categoryId = categoryCodes.getCode(inputNum - 1);
        Output.printlnAsInfo(
                "クイズジャンルとして「" + categoryCodes.getEntryFromCode(categoryId) + "」が選択されました。");

        return categoryId;
    }

    /**
     * 出題予定のクイズリストから１問取り出し、出題と回答判定を行う
     */
    private static void nextQuiz() {
        int inputNum; // 標準入力を数値型に変換した値
        Question question;

        quizGame.nextQuiz();
        question = quizGame.getCurrentQuestion();

        // 出題
        while (true) {
            int selectNum = question.getSelectNum();

            System.out.printf("第 %d 問\n", quizGame.getCurrentQuestionNum());
            System.out.printf("「%s」の作者は誰でしょう？\n", question.getQuestion());

            for (int i = 0; i < selectNum; i++) {
                System.out.printf("%2d. %s\n", i + 1, question.getSelectString(i));
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
        if (quizGame.judgeQuiz(inputNum)) {
            Output.printlnAsCollect();
        } else {
            Output.printlnAsIncollect();
        }

        System.out.printf("解説：「%s」の作者は「%s」です。\n\n", question.getQuestion(), question.getAnswer());
    }
}
