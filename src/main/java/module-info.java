module com.example.lb4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.housemanagement to javafx.fxml;
    exports com.housemanagement;
}