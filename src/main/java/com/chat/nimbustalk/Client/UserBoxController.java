package com.chat.nimbustalk.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

public class UserBoxController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Circle countCircle;

    @FXML
    private Label countLabel;

    public void setUsername(String username) {
        usernameLabel.setText(username);
    }

}

