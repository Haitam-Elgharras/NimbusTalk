package com.chat.nimbustalk.Client;

import com.chat.nimbustalk.Client.connector.ServerConnector;
import com.chat.nimbustalk.Server.dao.entities.Message;
import com.chat.nimbustalk.Server.dao.entities.User;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

public class HomeController extends Thread implements Initializable {
    @FXML
    public Label clientName;
    @FXML
    public Button chatBtn;
    @FXML
    public Pane chat;

    @FXML
    public BorderPane chatScene;
    @FXML
    public BorderPane profileScene;

    @FXML
    public TextField msgField; // where the user types the message
    @FXML
    public VBox msgRoom;// where the messages are displayed
    @FXML
    public Label usernameLabel;
    @FXML
    public Label online;
    @FXML
    public Label fullName;
    @FXML
    public Label email;
    @FXML
    public Label phoneNo;
    @FXML
    public Label gender;
    @FXML
    public Label username;
    @FXML
    public Pane profile;
    @FXML
    public Button profileBtn; // to toggle between chat and profile
    @FXML
    public TextField fileChoosePath;
    @FXML
    public ImageView proImage;
    @FXML
    public Circle showProPic;
    @FXML
    public HBox userBox;
    @FXML
    public HBox userBox1;
    @FXML
    public VBox userBoxContainer;
    @FXML
    public HBox searchBox;
    @FXML
    public ImageView btnEmoji;
    @FXML
    public ImageView profileIconHome;
    @FXML
    public ImageView profileIcon;
    @FXML
    private TextFlow emojiList;
    @FXML
    private ScrollPane clientListScroll;
    @FXML
    private ListView listView;
    @FXML
    private VBox clientListBox;

    private FileChooser fileChooser;
    private File filePath;
    public boolean toggleChat = false, toggleProfile = false;

    private boolean privateChatMode = false;
    private User privateChatUser = null;


    BufferedReader reader; // to read the messages from the server
    PrintWriter writer; // to write the messages to the server
    Socket socket;  // to connect with the server


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chatScene.setOpacity(1);
        chatScene.toFront();
        profileScene.setOpacity(0);
        profileScene.toBack();

        for(Node text : emojiList.getChildren()){
            text.setOnMouseClicked(event -> {
                msgField.setText(msgField.getText()+" "+((Text)text).getText());
                emojiList.setVisible(false);
            });
        }
        connectSocket();
    }

    public void connectSocket() {
        try {
            // 1. Create a Socket object and connect to the server Socket(server ip, port number)
            socket = new Socket("localhost", 8889);
            

            // 2. Create a BufferedReader to read data from the server
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 3. Create a PrintWriter to send data to the server
            writer = new PrintWriter(socket.getOutputStream(), true);

            // 4. Send the username to the server
            //            writer.println(Controller.user.getFullName());


            // instead of sending the username to the server we will send the id
            writer.println(Controller.user.getUsername());
            
            


            // 5. Start a new thread for background communication
            this.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            // Continuously read messages from the server
            while (true) {
                // 1. Read a line of text from the server(clientHandler)
                String msg = reader.readLine();
                System.out.println("msg: " + msg);

                if(msg.equalsIgnoreCase("updateListOfUsers")) {
                    
                    // 2. Read the list of clients from the server
                    Platform.runLater(() -> {
                        // read the new list of users from the server
                        try {
                            Controller.users = (ArrayList<User>) ServerConnector.getController().getAllUsers();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        // users from the database
                        updateUsersList(Controller.users);
                    });
                    continue;
                }

                // 2. Split the message into tokens
                String[] tokens = msg.split(":");
                String cmd = tokens[0];

                // 3. Extract the full message (excluding the command)
                StringBuilder fullMsg = new StringBuilder();
                String tokenToAppend = tokens.length > 2 ? tokens[1]+":"+tokens[2] : tokens[1];
                System.out.println("tokenToAppend: " + tokenToAppend);
                fullMsg.append(tokenToAppend);

                // 4. Print the command and the full message
                
                

                // 5. Skip messages sent by the current user because they are already displayed
                if (cmd.equalsIgnoreCase(Controller.user.getUsername())) {
                    {
                        continue;
                    }
                } else if (fullMsg.toString().trim().equalsIgnoreCase("bye")) {
                    // 6. If the message indicates "bye," exit the loop and close resources
                    break;
                }

                // 7. Call the update method to update the UI
                Platform.runLater(() -> {
                    try {
                        System.out.println("cmd ? : " + cmd);
                        update(cmd, fullMsg.toString().trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 8. Close the BufferedReader, PrintWriter, and Socket
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean update(String username, String message) {

        String fullname = "";
        String[] tokens = message.split(":");
        fullname = tokens[0];

        System.out.println("from update method: " + username + " " + message);

        boolean isPrivate = Controller.user.getUsername().equalsIgnoreCase(username) && tokens.length > 1 && tokens[1].startsWith("@");


        if (isPrivate) {
            tokens[0] = privateChatMode ? "You :" : "You to " + tokens[0] + " :";
            tokens[1] ="";
            message = String.join(" ", tokens);
        }
        else if (Controller.user.getUsername().equalsIgnoreCase(username)) {
            tokens[0] = "You :";
            message = String.join(" ", tokens);
        }

        if(!Controller.user.getUsername().equalsIgnoreCase(username) && message.split(":").length == 2){
                message = message.split(":")[1];
        }

        if(username.contains("(private")){
            System.out.println("contains private :" + username);
            String tmp = username.replace("(private)", "").trim();
            System.out.println("tmp: " + tmp);
            try {
                System.out.println("it's in try");
                fullname = Controller.users.stream().filter(u -> u.getUsername().equals(tmp)).findFirst().orElse(null).getFullName();
                System.out.println("fullname of sender: " + fullname);
            }
            catch (Exception e){
                fullname = tmp;
                e.printStackTrace();
            }
            if(!privateChatMode) {
                System.out.println("not in private chat mode " + privateChatMode);
                fullname = fullname + " (private)";
            }
        }
        Text text = new Text(message);

        text.setFill(Color.WHITE);
        text.getStyleClass().add("message");
        TextFlow tempFlow = new TextFlow();

        username=username.replace("(private)", "").trim();
        if (!Controller.user.getUsername().equalsIgnoreCase(username)) {
            System.out.println("From testing of not receiving messages from other users while in private chat mode");

            // to not receive messages from other users while in private chat mode
            if(!isPrivate && privateChatMode ){
                System.out.println("From last if");
                assert privateChatUser != null;
                System.out.println("privateChatUser: " + privateChatUser.getUsername());
                System.out.println("username: " + username);
                if(!privateChatUser.getUsername().equalsIgnoreCase(username)){
                    return false;
                }
            }
            System.out.println("fullname: " + fullname);
            Text txtName = new Text(fullname + "\n");
            txtName.getStyleClass().add("txtName");
            tempFlow.getChildren().add(txtName);
        }

        tempFlow.getChildren().add(text);
        tempFlow.setMaxWidth(200);

        TextFlow flow = new TextFlow(tempFlow);

        HBox hbox = new HBox(12);

        Circle img = new Circle(32, 32, 16);
        try {
            String path = new File("src/main/java/com/chat/nimbustalk/icons/userConv.png").toURI().toString();
            img.setFill(new ImagePattern(new Image(path)));
        } catch (Exception ex) {
            ex.printStackTrace();  // Handle the exception or print the stack trace for debugging
        }
        img.getStyleClass().add("imageView");

        if (!Controller.user.getUsername().equalsIgnoreCase(username)) {
            tempFlow.getStyleClass().add("tempFlowFlipped");
            flow.getStyleClass().add("textFlowFlipped");
            msgRoom.setAlignment(Pos.TOP_LEFT);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.getChildren().add(img);
            hbox.getChildren().add(flow);

        } else {
            text.setFill(Color.WHITE);
            tempFlow.getStyleClass().add("tempFlow");
            flow.getStyleClass().add("textFlow");
            hbox.setAlignment(Pos.BOTTOM_RIGHT);
            hbox.getChildren().add(flow);
            hbox.getChildren().add(img);
        }

        hbox.getStyleClass().add("hbox");
        Platform.runLater(() -> msgRoom.getChildren().addAll(hbox)); // Use msgRoom instead of chatBox
        return true;
    }


    public void sendMessageByKey(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            send();
        }
    }

    public void handleSendEvent(MouseEvent event) {
        send();
    }
    public void send() {
        String recipient = null;
        String message = msgField.getText().trim();

        if (privateChatMode) {
            sendPrivateMessage("@" + privateChatUser.getUsername(), Controller.user.getFullName(), message);
        }
        else {
            if (message.startsWith("@")) {

                int spaceIndex = message.indexOf(" ");
                if (spaceIndex != -1) {
                    recipient = message.substring(0, spaceIndex);
                    System.out.println("recipient: from send method" + recipient);

                    message = message.substring(spaceIndex + 1);
                }
            }

            if (recipient != null && !recipient.isEmpty()) {
                {
                    // retieve the full name of the recipient
                    String finalRecipient = recipient;
                    String fullname = Objects.requireNonNull(Controller.users.stream()
                            .filter(u -> u.getUsername().equals(finalRecipient.substring(1)))
                            .findFirst().orElse(null)).getFullName();

                    System.out.println("verify the recipient: " + recipient);
                    sendPrivateMessage(recipient, fullname, message);
                }
            } else {
                // Regular public message
                String fullMessage = Controller.user.getFullName() + ": " + message;

                writer.println(Controller.user.getUsername() + ":" + fullMessage);
                update(Controller.user.getUsername(), fullMessage); // Use the update method here
                msgField.setText("");
                if (message.equalsIgnoreCase("BYE") || message.equalsIgnoreCase("logout")) {
                    System.exit(0);
                }
            }
        }
    }

    public void sendPrivateMessage(String recipient, String fullname, String message) {
        String fullMessage = fullname + ":" + recipient + ":" + message;
        
        update(Controller.user.getUsername(), fullMessage); // Use the update method here
        System.out.println("whats sended to the sender: " + Controller.user.getUsername() + ":" + fullMessage);

        // send message to the server
        writer.println(Controller.user.getUsername() + ":"+recipient+": "+message);
//        writer.println(Controller.user.getUsername() + ":"+Controller.user.getFullName()+": "+message);

        System.out.println("whats sends to the server: " + Controller.user.getUsername() + ":"+recipient+": "+message);
        //Add message in DB
        Message m = new Message();
        m.setContent(message);
        m.setSender(Controller.user);
        String finalRecipient = recipient.substring(1);
        User receiver = Controller.users.stream().filter(u -> u.getUsername().equals(finalRecipient)).findFirst().orElse(null);
        m.setReceiver(receiver);
        try {
            ServerConnector.getController().addMessage(m);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        msgField.setText("");
        if (message.equalsIgnoreCase("BYE") || message.equalsIgnoreCase("logout")) {
            System.exit(0);
        }
    }

    @FXML
    void emojiAction(MouseEvent event) {
        emojiList.setVisible(!emojiList.isVisible());
    }


    public boolean updateUsersList(ArrayList<User> userList) {
        
        Platform.runLater(() -> {
            // Clear only user boxes, starting from index 2 (search box and separator)
            ObservableList items = listView.getItems();
            items.subList(0, items.size()).clear();

            // Add user boxes dynamically
            double layoutY = 0;
            for (User client : userList) {
                if (Controller.user.getUsername().equals(client.getUsername())) continue;

                HBox userBox = createUserBox(client);
                assert userBox != null;
                userBox.setLayoutY(layoutY);
                layoutY += 100; // Increment layoutY by 100 for the next user box

                // Add the user box to the list view
                listView.getItems().add(userBox);
                
            }
        });

        return true;
    }

    private HBox createUserBox(User client) {

        File file = new File("src/main/java/com/chat/nimbustalk/Client/UserBox.fxml");
        URL url;
        try {
             url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        FXMLLoader loader = new FXMLLoader(url);

        try {
            HBox userBox = loader.load();

            // Access the controller of the loaded FXML
            UserBoxController userBoxController = loader.getController();

            // Call the non-static method on the instance
            userBoxController.setUsernameLabel(client.getFullName());
            userBoxController.getHiddenUsername().setText(client.getUsername());

            // You can now return this HBox and use it as needed
            return userBox;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    // handle testUI button click event
    public void handleTestUI(MouseEvent event) {
        updateUsersList(Controller.users);
    }

    @FXML
    private void openGroupFXML() {
        try {
            // Load the FXML file
            File file = new File("src/main/java/com/chat/nimbustalk/Client/Group.fxml");
            URL url = file.toURI().toURL();
            Parent root = FXMLLoader.load(url);

            // Create a new scene
            Scene scene = new Scene(root);

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Another FXML");
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // handle list view click event
    @FXML
    public void handleListViewClick(MouseEvent event) {
        HBox userBox = (HBox) listView.getSelectionModel().getSelectedItem();
        if (userBox == null) return;
        else {
            String userName = ((Text) ((VBox) userBox.getChildren().get(1)).getChildren().get(0)).getText();

            User user = Controller.users.stream()
                    .filter(u -> u.getUsername().equals(userName))
                    .findFirst().orElse(null);

            if (user != null) {
                {
                    usernameLabel.setText(user.getFullName());
                    privateChatMode = true;
                    privateChatUser = user;
                }
                List<Message> messages = null;
                try {
                    messages = ServerConnector.getController().getAllMessages(Controller.user, user);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }

                msgRoom.getChildren().clear();

                // Check for null messages and created_at values
                if (messages != null) {
                    messages.stream()
                            .filter((m)->{
                                System.out.println("message" + m.getContent());
                                System.out.println("message" + m.getCreated_at());
                                return true;
                            })
                            .filter(m -> m.getCreated_at() != null)
                            .sorted(Comparator.comparing(Message::getCreated_at))
                            .forEach(m -> {
                                String message = m.getSender().getFullName() + ": " + m.getContent();
                                System.out.println(message);
                                System.out.println("update called");
                                update(m.getSender().getUsername(), message);
                            });
                }
            }
        }
    }


    public void handleProfileClick(MouseEvent event) {
        if(event.getSource().equals(profileIconHome)){
            System.out.println("profileIconHome");
            chatScene.setOpacity(0);
            chatScene.toBack();
            profileScene.setOpacity(1);
            profileScene.toFront();
            setProfile();
        }
        else if(event.getSource().equals(profileIcon)){
            System.out.println("profileIcon");
            chatScene.setOpacity(1);
            chatScene.toFront();
            profileScene.setOpacity(0);
            profileScene.toBack();

        }

    }

    public void setProfile() {
        for (User user : Controller.users) {
            if (Controller.user.getUsername().equalsIgnoreCase(user.getUsername())) {
                fullName.setText(user.getFullName());
                fullName.setOpacity(1);
                email.setText(user.getEmail());
                email.setOpacity(1);
                phoneNo.setText(user.getPhoneNumber());
                gender.setText(user.getGender());
                username.setText(user.getUsername());
            }
        }
    }


    public void chooseImageButton(ActionEvent actionEvent) {

    }

    public void saveImage(ActionEvent actionEvent) {

    }
}





