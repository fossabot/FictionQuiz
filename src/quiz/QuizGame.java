package quiz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import io.DatabaseSqlite3;
import io.Output;
import io.Properties;

/**
 * クイズゲームを管理・処理するクラス
 * @author komoto
 *
 */
public class QuizGame {
    /** 作品リスト */
    private final List<Sakuhin> sakuhinList;

    /** 作者リスト */
    private final CodeList authorList;

    /** 作成したクイズ情報を格納するキュー（FIFOで管理） */
    private final Queue<Question> quizQueue;

    /** データベース情報 */
    private final DatabaseSqlite3 db;

    /** 現在扱っている問題を格納する変数 */
    private Question currentQuestion;

    /** 現在の出題数を表す変数 */
    private int currentQuestionNum = 0;

    /** 現在の正答数を表す変数 */
    private int currentAnswerNum = 0;

    /**
     * クイズゲームの初期化を行う。ここではデータベースに接続する。
     * @param dbFilePath データベースのファイルパス
     */
    public QuizGame(String dbFilePath) {
        sakuhinList = new ArrayList<Sakuhin>();
        authorList = new CodeList();
        quizQueue = new ArrayDeque<Question>();
        db = new DatabaseSqlite3(dbFilePath);
    }

    /**
     * 出題した問題数を返す
     * @return 出題した問題数
     */
    public int getCurrentQuestionNum() {
        return currentQuestionNum;
    }

    /**
     * 正解した回答数を返す
     * @return 正解した回答数
     */
    public int getCurrentAnswerNum() {
        return currentAnswerNum;
    }

    /**
     * 現在のクイズを返す
     * @return 現在のクイズ
     */
    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    /**
     * 作品ジャンルのリストを作成して返す
     * @return 作品ジャンルのリスト
     */
    public CodeList getCategoryCodes() {
//        List<Code> codes = new ArrayList<Code>();
        CodeList codeList = new CodeList();
        ResultSet rs;

        rs = db.executeQuery("SELECT * FROM CategoryTable");

        try {
            while (rs.next()) {
                codeList.add(Integer.parseInt(rs.getString("id")), rs.getString("category"));
            }
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }

        return codeList;
    }

    /**
     * 指定されたジャンルの作品に関する問題を作成する。
     * まず、データベースに接続・抽出し、作品リスト {@code sakuhinList}と作者リスト {@code authorList}を作成する。
     * 次に、作品リスト {@code sakuhinList} を元に、クイズキュー {@code quizQueue} を作成する。
     * @param categoryId 指定されたジャンルのコード番号（データベースの {@code CategoryTable} テーブルで定義）
     */
    public void generateSakuhinList(int categoryId) {
        // 作品リストと作者リストを初期化する
        sakuhinList.clear();
        authorList.clear();

        String sql = "SELECT title_name, AuthorTable.id AS author_code, AuthorTable.name AS author_name FROM SakuhinTable "
                + "INNER JOIN AuthorTable ON SakuhinTable.author_code = AuthorTable.id "
                + "WHERE category_code = " + categoryId;
        ResultSet rs = db.executeQuery(sql);
        try {
            while (rs.next()) {
                String title = rs.getString("title_name");
                String authorName = rs.getString("author_name");
                int authorCode = rs.getInt("author_code");

                sakuhinList.add(new Sakuhin(title, authorName));
                
                if(!(authorList.containsByCode(authorCode))) {    // リストに重複が発生しないように追加する。
                    authorList.add(authorCode, authorName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        db.disconnectDB();
    }

    /**
     * 出題するクイズを作成する
     * @param prop プロパティファイルの情報
     * @return 作成したクイズの問題数
     */
    public int generateQuiz(Properties prop) {
        int questionNum = 0; // 出題する問題数

        Output.printlnAsInfo("出題するクイズを生成します。");

        //        generateQuizQueue(prop.questionNum, prop.selectNum);
        if (sakuhinList.isEmpty()) {
            throw new NullPointerException("作品リストが作成されていません。");
        }

        if (authorList.isEmpty()) {
            throw new NullPointerException("作者リストが作成されていません。");
        }

        currentQuestionNum = 0;
        currentAnswerNum = 0;
        quizQueue.clear();

        Collections.shuffle(sakuhinList);

        for (int i = 0; i < sakuhinList.size() && i < prop.getQuestionNum(); i++) {
            quizQueue.add(new Question(sakuhinList.get(i).getTitle(),
                    sakuhinList.get(i).getAuthor(),
                    authorList, prop.getSelectNum()));
        }

        // 出題する問題数を設定する
        // 原則はプロパティファイルの値を設定するが、
        // 作成された問題が設定された問題数よりも少ない場合は、作成された問題数を出題数として設定する。
        if (quizQueue.size() < prop.getQuestionNum()) {
            questionNum = quizQueue.size();
            Output.printlnAsWarning("データベース件数が少ないため、出題数は変更されました。出題数： " + questionNum);
        } else {
            questionNum = prop.getQuestionNum();
        }

        Output.printlnAsInfo("クイズの準備が完了しました。出題数：" + questionNum);
        System.out.println("");

        return questionNum;
    }

    /**
     * 次のクイズをセットする。
     * 出題予定のクイズリストから１問取り出し、{@code currentQuiz} にセットする
     * @return クイズがセットされたときは {@code true} 、次に出題するクイズがない場合は {@code false} を返す。
     */
    public Boolean nextQuiz() {
        if (quizQueue.isEmpty()) {
            Output.printlnAsError("問題リストが空のため、出題することができません。\nクイズキューを再生成する必要があります。");
            return false;
        }

        currentQuestionNum++;
        currentQuestion = quizQueue.poll();

        return true;
    }

    /**
     * 入力された回答の正否を判定する
     * @param selectNum 入力された選択肢の番号
     * @return 正解の場合は{@code true}、不正解の場合は{@code false}を返す。
     */
    public Boolean judgeQuiz(int selectNum) {
        String answerString;

        try {
            answerString = currentQuestion.getSelectString(selectNum - 1);
        } catch (Exception e) {
            Output.printlnAsError("想定されていない選択肢番号が入力されました。");
            return false;
        }

        // 正解か判定する
        if (answerString.equals(currentQuestion.getAnswer())) {
            currentAnswerNum++;
            return true;
        } else {
            return false;
        }
    }
}
