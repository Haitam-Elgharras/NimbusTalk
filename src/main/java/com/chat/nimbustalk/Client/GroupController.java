package com.chat.nimbustalk.Client;

import com.chat.nimbustalk.Client.connector.ServerConnector;
import com.chat.nimbustalk.Server.dao.entities.Group;
import com.chat.nimbustalk.Server.dao.entities.User;
import com.chat.nimbustalk.Server.dao.entities.UserGroup;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
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
            for (User u: ServerConnector.getController().getAllUsers()) {
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
        try {
            //Form Validation
            if (groupeName.getText().trim().equalsIgnoreCase("")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Group Creation");
                alert.setHeaderText("Invalid Form");
                alert.setContentText("Enter a group name");
                alert.show();
            }
            else if (ServerConnector.getController().getGroupByName(groupeName.getText()).getId() != 0){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Group Creation");
                alert.setHeaderText("Invalid Form");
                alert.setContentText("Group name already used");
                alert.show();
            }
            else if (userList.getSelectionModel().getSelectedItems().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Group Creation");
                alert.setHeaderText("Invalid Form");
                alert.setContentText("Select at least one group member");
                alert.show();
            }
            else {
                //Add Group to DB
                Group g = new Group();
                g.setName(groupeName.getText());
                ServerConnector.getController().addGroup(g);

                //Add Group creator to UserGroup
                Group group = ServerConnector.getController().getGroupByName(groupeName.getText());
                UserGroup userGroupe = new UserGroup();
                userGroupe.setUser(Controller.user);
                userGroupe.setGroup(group);
                ServerConnector.getController().addUserToGroup(userGroupe);

                //Add Selected users in UserGroup
                ObservableList<Label> selectedUsers = userList.getSelectionModel().getSelectedItems();
                for (Label l:selectedUsers) {
                    User u = ServerConnector.getController().getUserByUsername(l.getText());
                    UserGroup userGroup = new UserGroup();
                    userGroup.setUser(u);
                    userGroup.setGroup(group);
                    ServerConnector.getController().addUserToGroup(userGroup);
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Group Creation");
                alert.setHeaderText(groupeName.getText() + " is created successfully");
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
