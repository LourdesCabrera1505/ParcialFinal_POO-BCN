module org.example.bcn {
    requires javafx.controls;
    requires javafx.fxml;
    requires  java.sql;

    opens org.example.bcn to javafx.fxml;
    exports org.example.bcn;
}