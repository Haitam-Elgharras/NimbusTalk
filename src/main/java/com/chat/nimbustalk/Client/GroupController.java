package com.chat.nimbustalk.Client;

import com.chat.nimbustalk.Client.connector.ServerConnector;
import com.chat.nimbustalk.Server.dao.entities.Group;
import com.chat.nimbustalk.Server.dao.entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class GroupController implements Initializable {
    @FXML
    TextField groupeName;

    @FXML
    ListView userList;

    @FXML
    Button createButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            for (User u: ServerConnector.getControler().getAllUsers()) {
                if (u.getId() != Controller.user.getId()){
                    userList.getItems().add(new Label(u.getFullName()));
                    System.out.println(u.getFullName());
                }
            }
            userList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void buttonHandler(){
        Group group = new Group();
        group.setName(groupeName.getText());
        ObservableList<Label> selectedUsers = userList.getSelectionModel().getSelectedItems();
        for (Label l:selectedUsers) {
            System.out.println(l.getText());
        }
        try {
            ServerConnector.getControler().addGroup(group);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
