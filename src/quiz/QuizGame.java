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

/**
 * クイズゲームを管理・処理するクラス
 * @author komoto
 *
 */
public class QuizGame {
    /** 現在の出題数を表す変数 */
    private int currentQuestionNum = 0;

    /** 現在の正答数を表す変数 */
    private int currentAnswerNum = 0;

    /** 現在扱っている問題を格納する変数 */
    private Question currentQuiz = null;

    /** データベース情報を格納する変数 */
    DatabaseSqlite3 db = null;

    /** 作品リスト */
    private List<Sakuhin> sakuhinList = new ArrayList<Sakuhin>();

    /** 作者リスト */
    //    private List<String> authorList = new ArrayList<String>();
    private List<Code> authorList = new ArrayList<Code>();

    /** 作成したクイズ情報を格納するキュー（FIFOで管理） */
    private Queue<Question> quizQueue = new ArrayDeque<Question>();

    /**
     * クイズゲームの初期化を行う。ここではデータベースに接続する。
     * @param dbFilePath データベースのファイルパス
     */
    public QuizGame(String dbFilePath) {
        db = new DatabaseSqlite3(dbFilePath);
    }

    /**
     * 作成されている問題の個数を返す
     * @return 作成された問題の個数
     */
    public int getQuizQueuSize() {
        return quizQueue.size();
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
        return currentQuiz;
    }
    
    /**
     * 作品ジャンルのリストを作成して返す
     * @return 作品ジャンルのリスト
     */
    public List<Code> getCategoryCodes() {
        List<Code> codes = new ArrayList<Code>();
        ResultSet rs;

        rs = db.executeQuery("SELECT * FROM CategoryTable");

        try {
            while (rs.next()) {
                codes.add(new Code(Integer.parseInt(rs.getString("id")),
                        rs.getString("category")));
            }
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }

        return codes;
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

                if (!(ControlCodeList.containsByCode(authorList, authorCode))) {
                    authorList.add(new Code(authorCode, authorName)); // リストに重複が発生しないように追加する。
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        db.disconnectDB();
    }

    /**
     * クイズを生成する
     * @param quesitonNum 作成するクイズの個数
     * @param selectNum 作成する選択肢の数
     */
    public void generateQuiz(int quesitonNum, int selectNum) {
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

        for (int i = 0; i < sakuhinList.size() && i < quesitonNum; i++) {
            quizQueue.add(new Question(sakuhinList.get(i).getTitle(),
                    sakuhinList.get(i).getAuthor(),
                    authorList, selectNum));
        }
    }

    /**
     * クイズを {@code currentQuiz} にセットする
     * @return クイズがセットされたときは {@code true} 、キューが空などでセットできなかったときは {@code false} を返す。
     */
    public Boolean setQuiz() {
        if (quizQueue.isEmpty()) {
            Output.printlnAsError("問題リストが空のため、出題することができません。\nクイズキューを再生成する必要があります。");
            return false;
        }

        currentQuestionNum++;
        currentQuiz = quizQueue.poll();

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
            answerString = currentQuiz.getSelectString(selectNum - 1);
        } catch (Exception e) {
            Output.printlnAsError("想定されていない選択肢番号が入力されました。");
            return false;
        }

        // 正解か判定する
        if (answerString.equals(currentQuiz.getAnswer())) {
            Output.printlnAsCollect();
            currentAnswerNum++;
            return true;
        } else {
            Output.printlnAsIncollect();
            return false;
        }
    }
}
