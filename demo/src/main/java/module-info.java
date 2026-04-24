module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    opens com.lostfound.controllers to javafx.fxml;
    opens com.lostfound.models to javafx.base;

    exports com.lostfound;
    exports com.lostfound.controllers;
    exports com.lostfound.models;
}
