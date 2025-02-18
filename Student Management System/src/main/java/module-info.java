module com.example.list {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens FrontEnd.GUI to javafx.fxml;
    opens controllers to javafx.fxml;

    exports com.example.list to javafx.graphics;
    exports controllers;

}
