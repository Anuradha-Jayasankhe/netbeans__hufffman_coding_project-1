module org.example.encode {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.encode to javafx.fxml;
    exports org.example.encode;
}