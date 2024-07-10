module org.example.bcn {
    requires javafx.controls;
    requires javafx.fxml;
    requires  java.sql;

    opens org.example.bcn.Modelos to javafx.base;
    opens org.example.bcn.launcher to javafx.fxml;
    exports org.example.bcn.launcher;
    exports org.example.bcn.Controller;
    opens org.example.bcn.Controller to javafx.fxml;
}