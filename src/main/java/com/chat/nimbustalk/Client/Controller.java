package com.chat.nimbustalk.Client;

import com.chat.nimbustalk.Client.connector.ServerConnector;
import com.chat.nimbustalk.Server.rmi.ChatController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import com.chat.nimbustalk.Server.dao.entities.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Controller {
    @FXML
    public Pane pnSignIn;
    @FXML
    public Pane pnSignUp;
    @FXML
    public Button btnSignUp;
    @FXML
    public Button getStarted;
    @FXML
    public ImageView btnBack;
    @FXML
    public TextField regName;
    @FXML
    public TextField regPass;
    @FXML
    public TextField regEmail;
    @FXML
    public TextField regFirstName;
    @FXML
    public TextField regPhoneNo;
    @FXML
    public RadioButton male;
    @FXML
    public RadioButton female;
    @FXML
    public Label controlRegLabel;
    @FXML
    public Label success;
    @FXML
    public Label goBack;
    @FXML
    public TextField userName;
    @FXML
    public TextField passWord;
    @FXML
    public Label loginNotifier;
    @FXML
    public Label nameExists;
    @FXML
    public Label checkEmail;

    public static User user;
    public static ArrayList<User> loggedInUser = new ArrayList<>();
    public static  ArrayList<User>  users = new ArrayList<>();

    public void registration() throws RemoteException {
       if (!regName.getText().equalsIgnoreCase("")
                && !regPass.getText().equalsIgnoreCase("")
                && !regEmail.getText().equalsIgnoreCase("")
                && !regFirstName.getText().equalsIgnoreCase("")
                && !regPhoneNo.getText().equalsIgnoreCase("")
                && (male.isSelected() || female.isSelected())) {
            if(checkUser(regName.getText())) {
                if(checkEmail(regEmail.getText())) {
                    User u = new User();
                    u.setFullName(regFirstName.getText());
                    u.setEmail(regEmail.getText());
                    u.setPassword(regPass.getText());
                    u.setPhoneNumber(regPhoneNo.getText());
                    if (male.isSelected()) {
                        u.setGender("M");
                    } else {
                        u.setGender("F");
                    }
                    //Added user in DB
                    ServerConnector.getControler().addUser(u);
                    goBack.setOpacity(1);
                    success.setOpacity(1);
                    makeDefault();
                    if (controlRegLabel.getOpacity() == 1) {
                        controlRegLabel.setOpacity(0);
                    }
                    if (nameExists.getOpacity() == 1) {
                        nameExists.setOpacity(0);
                    }
                } else {
                    checkEmail.setOpacity(1);
                    setOpacity(nameExists, goBack, controlRegLabel, success);
                }
            } else {
                nameExists.setOpacity(1);
                setOpacity(success, goBack, controlRegLabel, checkEmail);
            }
        } else {
            controlRegLabel.setOpacity(1);
            setOpacity(success, goBack, nameExists, checkEmail);
        }

        // register two users for testing
        /*User newUser = new User();
        newUser.name = "haitam";
        newUser.password = "123";
        newUser.email = "hh@hh";
        newUser.fullName = "Haitam";
        newUser.phoneNo ="5121521";
        newUser.gender="male";

        User newUser1 = new User();
        newUser1.name = "ilyas";
        newUser1.password = "123";
        newUser1.email = "hh@hh";
        newUser1.fullName = "Haitam";
        newUser1.phoneNo ="5121521";
        newUser1.gender="male";

        User newUser2 = new User();
        newUser2.name = "youssef";
        newUser2.password = "123";
        newUser2.email = "hh@hh";
        newUser2.fullName = "youssef";
        newUser2.phoneNo ="5121521";
        newUser2.gender="male";

        users.add(newUser1);
        users.add(newUser);*/

    }

    public void login() throws RemoteException {
        //At form openning: creation of chatController instance from server side

        users = (ArrayList<User>) ServerConnector.getControler().getAllUsers();
        String username = userName.getText();
        String password = passWord.getText();
        boolean login = false;
        for (User x : users) {
            System.out.println(x.getFullName() + " " + x.getPassword());
            if (x.getFullName().equalsIgnoreCase(username) && x.getPassword().equals(password)) {
                login = true;
                //Created User
                user = x;
                loggedInUser.add(x);
                System.out.println(x.getFullName());
                break;
            }
        }
        if (login) {
            changeWindow();
        } else {
            loginNotifier.setOpacity(1);
        }
    }

    private void setOpacity(Label a, Label b, Label c, Label d) {
        if(a.getOpacity() == 1 || b.getOpacity() == 1 || c.getOpacity() == 1 || d.getOpacity() == 1) {
            a.setOpacity(0);
            b.setOpacity(0);
            c.setOpacity(0);
            d.setOpacity(0);
        }
    }


    private void setOpacity(Label controlRegLabel, Label checkEmail, Label nameExists) {
        controlRegLabel.setOpacity(0);
        checkEmail.setOpacity(0);
        nameExists.setOpacity(0);
    }

    private boolean checkUser(String username) {
        for(User user : users) {
            if(user.getFullName().equalsIgnoreCase(username)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkEmail(String email) {
        for(User user : users) {
            if(user.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
    }

    private void makeDefault() {
        regName.setText("");
        regPass.setText("");
        regEmail.setText("");
        regFirstName.setText("");
        regPhoneNo.setText("");
        male.setSelected(true);
        setOpacity(controlRegLabel, checkEmail, nameExists);
    }

    public void changeWindow() {
        try {
            Stage stage = (Stage) userName.getScene().getWindow();

            File file = new File("src/main/java/com/chat/nimbustalk/Client/Home.fxml");
            URL url = file.toURI().toURL();
            Parent root = FXMLLoader.load(url);
            stage.setScene(new Scene(root));
            stage.setTitle(user.getFullName() + "");
            stage.setOnCloseRequest(event -> {
                System.exit(0);
            });
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource().equals(btnSignUp)) {
            pnSignUp.setOpacity(1);
            pnSignUp.toFront();
        }
        if (event.getSource().equals(getStarted)) {
            pnSignIn.setOpacity(1);
            pnSignIn.toFront();
        }
        loginNotifier.setOpacity(0);
        userName.setText("");
        passWord.setText("");
    }

    @FXML
    private void handleMouseEvent(MouseEvent event) {
        if (event.getSource() == btnBack) {
            pnSignIn.setOpacity(1);
            pnSignIn.toFront();
        }
        regName.setText("");
        regPass.setText("");
        regEmail.setText("");
    }
}