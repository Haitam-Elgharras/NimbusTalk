package com.chat.nimbustalk.Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        File file = new File("src/main/java/com/chat/nimbustalk/Client/Sample.fxml");
        URL url = file.toURI().toURL();
        Parent root = FXMLLoader.load(url);
        stage.setTitle("Nimbus Talk");
        stage.getIcons().add(new javafx.scene.image.Image("file:src/main/java/com/chat/nimbustalk/icons/logo.png"));
        stage.setScene(new Scene(root, 330, 560));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}