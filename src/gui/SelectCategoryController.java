package gui;

import java.net.URL;
import java.util.ResourceBundle;

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

    /** ジャンルを表すボタン */
    @FXML
    private Button categoryButton1;

    /** ジャンルを表すボタン */
    @FXML
    private Button categoryButton2;

    /**
     * クイズのジャンルリストを生成し、画面表示の初期化を行う
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CategoryList categoryList = new CategoryList(Main.getInstance().getDB());

        GameValues gameValues = Main.getInstance().getGameValues();
        gameValues.setCategoryCodes(categoryList);

        categoryButton1.setText(categoryList.getEntry(0));
        categoryButton1.setVisible(true);
        categoryButton2.setText(categoryList.getEntry(1));
        categoryButton2.setVisible(true);
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
        Main.getInstance().gameView();
    }
}
