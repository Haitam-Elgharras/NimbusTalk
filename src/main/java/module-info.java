module com.nimbus.nimbustalk {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.rmi;

    requires org.kordamp.bootstrapfx.core;
    requires mysql.connector.java;

    opens com.chat.nimbustalk to javafx.fxml;
    exports com.chat.nimbustalk.Server;
    exports com.chat.nimbustalk.Client;
    exports com.chat.nimbustalk.Server.rmi;
    opens com.chat.nimbustalk.Client to javafx.fxml;
}