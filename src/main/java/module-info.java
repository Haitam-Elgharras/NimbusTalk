module com.nimbus.nimbustalk {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.chat.nimbustalk to javafx.fxml;
    exports com.chat.nimbustalk;
}