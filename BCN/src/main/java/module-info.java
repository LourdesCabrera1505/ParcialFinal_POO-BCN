module org.example.bcn {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.bcn to javafx.fxml;
    exports org.example.bcn;
}