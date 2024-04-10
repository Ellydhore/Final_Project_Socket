module com.example.final_project_socket {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.final_project_socket to javafx.fxml;
    opens com.example.final_project_socket.fxml_controller to javafx.fxml;
    exports com.example.final_project_socket;

    // Additional Exports
    exports com.example.final_project_socket.fxml_controller to javafx.fxml;
}