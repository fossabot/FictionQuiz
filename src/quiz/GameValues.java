package quiz;

import io.Properties;

/**
 * ゲームを管理するための情報を定義するクラス
 * @author komoto
 *
 */
public class GameValues {
    /** リソースディレクトリのパスを表す定数 */
    private final String RES_DIR_PATH = "./resource";

    /** プロパティファイル名を表す定数 */
    private final String PROPERTIES_FILE = "common";

    /** プロパティファイルの読込み結果を格納する変数 */
    private Properties prop;

    /** ジャンルのコード表を格納する変数 */
    private CategoryList categoryCodes;

    public String getResDirPath() {
        return RES_DIR_PATH;
    }

    public String getPropertiesFile() {
        return PROPERTIES_FILE;
    }

    public Properties getProperties() {
        return prop;
    }

    /**
     * デフォルトのプロパティファイルを読み込む
     * デフォルトのプロパティファイルは、{@code RES_DIR_PATH} と {@code PROPERTIES_FILE} で定義される。
     */
    public void setProperties() {
        this.prop = new Properties(RES_DIR_PATH, PROPERTIES_FILE);
    }

    /**
     * プロパティファイルを読み込む
     * @param dir プロパティファイルが保存されているディレクトリ
     * @param file プロパティファイルのファイル名
     */
    public void setProperties(String dir, String file) {
        this.prop = new Properties(dir, file);
    }

    public CategoryList getCategoryCodes() {
        return categoryCodes;
    }

    public void setCategoryCodes(CategoryList categoryCodes) {
        this.categoryCodes = categoryCodes;
    }

}
