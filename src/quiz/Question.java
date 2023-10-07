package quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * クイズ１問を定義するクラス
 * @author komoto
 *
 */
public class Question {
    
    /** 問題を表す変数 */
    private String questionString;

    /** 解答を表す変数 */
    private String answerString;

    /** 選択肢リストを格納する変数 */
    private List<String> selecStrings = new ArrayList<String>();
    
    /**
     * 問題、解答、選択肢を作成する。
     * 選択肢は、解答 {@code answer} と作者リスト {@code authorList} を元に作成する。
     * @param question 問題
     * @param answer 解答
     * @param authorList 作者リスト
     * @param selectNum 作成する選択肢の個数
     */
    public Question(String question, String answer, List<Code> authorList, int selectNum) {
        this.questionString = question;
        this.answerString   = answer;
        
        Collections.shuffle(authorList);
        
        // アタリを選択肢に追加する
        selecStrings.add(answer);
        
        // ハズレを選択肢に追加する
        for (int i = 0; selecStrings.size() < selectNum && i < authorList.size(); i++) {
            String authorString = authorList.get(i).getEntry();

            if (!(selecStrings.contains(authorString))) {
                selecStrings.add(authorString);    // リストに重複が発生しないように追加する。
            }
        }

        // 選択肢１が必ず答えになるため、選択肢を並び替える。
        Collections.shuffle(selecStrings);
    }

    /**
     * 問題を出題する
     * @return 問題文の文字列
     */
    public String getQuestion() {
        return questionString;
    }

    /**
     * 解答を返す
     * @return 問題に対応する解答の文字列
     */
    public String getAnswer() {
        return answerString;
    }

    /**
     * 選択肢の個数を返す
     * @return 選択肢の個数
     */
    public int getSelectNum() {
        return selecStrings.size();
    }

    /**
     * 指定された番号の選択肢を返す
     * @param i 選択肢の番号
     * @return 選択肢番号 {@code i} に対応する選択肢の文字列
     */
    public String getSelectString(int i) {
        return selecStrings.get(i);
    }
}

