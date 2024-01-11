package com.chat.nimbustalk.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class UserBoxController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Text hiddenUsername; // Add this line

    @FXML
    private ImageView userListProfile;

    @FXML
    private Label messageLabel;

    @FXML
    private Circle countCircle;

    @FXML
    private Label countLabel;

    public void setUsernameLabel(String username) {
        usernameLabel.setText(username);
    }

    public Label getUsernameLabel() {
        return usernameLabel;
    }

    public Text getHiddenUsername() {
        return hiddenUsername;
    }

    public void setHiddenUsername(Text hiddenUsername) {
        this.hiddenUsername = hiddenUsername;
    }

    public void setUserListProfile(Image image) {
        userListProfile.setImage(image);
    }
}

