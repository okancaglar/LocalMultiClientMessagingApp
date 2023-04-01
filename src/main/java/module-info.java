module com.example.localmessagingapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.localmessagingapp to javafx.fxml;
    exports com.example.localmessagingapp;
}