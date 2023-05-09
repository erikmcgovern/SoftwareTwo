module mcgovern.softwaretwo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens mcgovern.softwaretwo to javafx.fxml;
    exports mcgovern.softwaretwo;
    opens mcgovern.softwaretwo.controller to javafx.fxml;
    exports mcgovern.softwaretwo.controller;
    opens mcgovern.softwaretwo.model to javafx.fxml;
    exports mcgovern.softwaretwo.model;

}