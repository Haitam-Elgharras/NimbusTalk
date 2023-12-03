package com.chat.nimbustalk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        File homeFxml = new File("src/main/java/com/chat/nimbustalk/home.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(homeFxml.toURI().toURL());
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("NimbusTalk");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}