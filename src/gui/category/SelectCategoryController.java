package gui.category;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import gui.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import quiz.CategoryList;
import quiz.GameValues;

/**
 * ジャンル選択の画面表示を管理するクラス
 */
public class SelectCategoryController implements Initializable {

    /* ジャンル選択ボタンの最大数（FXMLで作成しているボタン数に合わせるため、GUI版のみ表示数を制限をしている） */
    private int MAX_CATEGORY_BUTTON_NUM = 6;
    
    /** ジャンルを表すボタン */
    @FXML
    private List<Button> categoryButtonList;

    /**
     * クイズのジャンルリストを生成し、画面表示の初期化を行う
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CategoryList categoryList = new CategoryList(Main.getInstance().getDB());

        GameValues gameValues = Main.getInstance().getGameValues();
        gameValues.setCategoryCodes(categoryList);

        // カテゴリー名があるものはボタンを表示、ないものはボタンを非表示にする
        for (int i = 0; i < MAX_CATEGORY_BUTTON_NUM; i++) {
            if (i < categoryList.size()) {
                categoryButtonList.get(i).setText(categoryList.getEntry(i));
                categoryButtonList.get(i).setVisible(true);
            } else {
                categoryButtonList.get(i).setVisible(false);
            }
        }
    }

    /**
     * ジャンルボタンが押されたときの動作
     * categoryButton1, categoryButton2 ともに、ボタン押下時にはどちらもこの関数が呼ばれる。
     * @param event イベントハンドラ
     */
    @FXML
    void onCategoryButtonAction(ActionEvent event) {
        String categoryString = ((Button) event.getSource()).getText();
        CategoryList categoryList = Main.getInstance().getGameValues().getCategoryCodes();

        int categoryId = categoryList.getCodeFromEntry(categoryString);
        Main.getInstance().setCategoryId(categoryId);
        Main.getInstance().showScene("Game");
    }
}
