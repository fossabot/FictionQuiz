package quiz;

/**
 * 作品クラス
 * @author komoto
 *
 */
public class Sakuhin {

    /** 作品のタイトルを表す変数 */
    private String titleString;

    /** 作品の作者を表す変数 */
    private String authorString;

    /**
     * 作品を定義する
     * @param titleString 作品のタイトル
     * @param authorString 作品の作者
     */
    public Sakuhin(String titleString, String authorString) {
        this.titleString = titleString;
        this.authorString = authorString;
    }

    /**
     * 作品のタイトルを返す
     * @return 作品のタイトル
     */
    public String getTitle() {
        return titleString;
    }

    /**
     * 作品の作者を返す
     * @return 作品の作者
     */
    public String getAuthor() {
        return authorString;
    }
}