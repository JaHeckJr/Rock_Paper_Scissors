module org.example.testjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.testjava to javafx.fxml;
    exports org.example.testjava;
}
