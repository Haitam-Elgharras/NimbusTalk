package com.chat.nimbustalk.Client;

import com.chat.nimbustalk.Client.connector.ServerConnector;
import com.chat.nimbustalk.Server.dao.entities.Group;
import com.chat.nimbustalk.Server.dao.entities.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class GroupController implements Initializable {
    @FXML
    TextField groupeName;

    @FXML
    VBox userList;

    @FXML
    Button createButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(Controller.user.getId());
        try {
            for (User u: ServerConnector.getControler().getAllUsers()) {
                if (u.getId() != Controller.user.getId()){
                    userList.getChildren().add(new Label(u.getFullName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void buttonHandler(){
        Group group = new Group();
        group.setName(groupeName.getText());
        try {
            ServerConnector.getControler().addGroup(group);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
