module SakuhinQuizGame {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    
    opens gui to javafx.graphics, javafx.fxml;
    
    // SQLite3
    requires java.sql;
	requires java.desktop;
	requires javafx.graphics;
}
