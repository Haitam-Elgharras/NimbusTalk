package com.chat.nimbustalk.Client;

import com.chat.nimbustalk.Client.connector.ServerConnector;
import com.chat.nimbustalk.Server.dao.Impl.MessageDaoImpl;
import com.chat.nimbustalk.Server.dao.entities.Message;
import com.chat.nimbustalk.Server.dao.entities.User;
import com.chat.nimbustalk.Server.service.Impl.IServiceMessageImpl;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

//import static Client.Controller.loggedInUser;
//import static Client.Controller.users;

public class HomeController extends Thread implements Initializable {
    @FXML
    public Label clientName;
    @FXML
    public Button chatBtn;
    @FXML
    public Pane chat;
    @FXML
    public TextField msgField; // where the user types the message
    @FXML
    public VBox msgRoom;// where the messages are displayed
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
    private TextFlow emojiList;
    @FXML
    private ScrollPane clientListScroll;
    @FXML
    private ListView listView;
    @FXML
    private VBox clientListBox;

    @FXML
    private Button testUI;
    private FileChooser fileChooser;
    private File filePath;
    public boolean toggleChat = false, toggleProfile = false;


    BufferedReader reader; // to read the messages from the server
    PrintWriter writer; // to write the messages to the server
    Socket socket;  // to connect with the server


    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
            System.out.println("Socket is connected with server!");

            // 2. Create a BufferedReader to read data from the server
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 3. Create a PrintWriter to send data to the server
            writer = new PrintWriter(socket.getOutputStream(), true);

            // 4. Send the username to the server
            writer.println(Controller.user.getFullName());


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

                if(msg.equalsIgnoreCase("updateListOfUsers")) {
                    System.out.println("updateListOfUsers");
                    // 2. Read the list of clients from the server
                    Platform.runLater(() -> {
                        // 2. Read the list of clients from the server
                        // users from the database
                        updateUsersList(Controller.users.stream().map(u -> {
                            System.out.println("from home controller " + u.getFullName());
                            return u.getFullName();
                        }).collect(Collectors.toCollection(ArrayList::new)));
                    });
                    continue;
                }

                // 2. Split the message into tokens
                String[] tokens = msg.split(" ");
                String cmd = tokens[0];

                // 3. Extract the full message (excluding the command)
                StringBuilder fullMsg = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fullMsg.append(tokens[i]).append(" ");
                }

                // 4. Print the command and the full message
                System.out.println(cmd);
                System.out.println(fullMsg);

                // 5. Skip messages sent by the current user because they are already displayed
                if (cmd.equalsIgnoreCase(Controller.user.getFullName() + ":")) {
                    continue;
                } else if (fullMsg.toString().trim().equalsIgnoreCase("bye")) {
                    // 6. If the message indicates "bye," exit the loop and close resources
                    break;
                }

                // 7. Call the update method to update the UI
                Platform.runLater(() -> {
                    try {
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

        String[] tokens = message.split(":");

        if (username.equalsIgnoreCase(Controller.user.getFullName()) && tokens.length > 1 && tokens[1].startsWith("@")) {
            tokens[0] = "You to " + tokens[1].substring(1) + " :";
            tokens[1] ="";
            message = String.join(" ", tokens);
        }
        else
        if (username.equalsIgnoreCase(Controller.user.getFullName())) {
            tokens[0] = "You :";
            message = String.join(" ", tokens);
        }

        Text text = new Text(message);

        text.setFill(Color.WHITE);
        text.getStyleClass().add("message");
        TextFlow tempFlow = new TextFlow();
        if (!Controller.user.getFullName().equals(username)) {
            Text txtName = new Text(username + "\n");
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

        if (!Controller.user.getFullName().equals(username)) {
            tempFlow.getStyleClass().add("tempFlowFlipped");
            flow.getStyleClass().add("textFlowFlipped");
            msgRoom.setAlignment(Pos.TOP_LEFT); // Use msgRoom instead of chatBox
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

        if (message.startsWith("@")) {
            System.out.println("private message");
            int spaceIndex = message.indexOf(" ");
            if (spaceIndex != -1) {
                recipient = message.substring(0, spaceIndex);
                System.out.println("recipient from send " + recipient);
                message = message.substring(spaceIndex + 1);
            }
        }

        if (recipient != null && !recipient.isEmpty()) {
            sendPrivateMessage(recipient, message);
        } else {
            // Regular public message
            String fullMessage = Controller.user.getFullName() + ": " + message;
            writer.println(fullMessage);
            update(Controller.user.getFullName(), fullMessage); // Use the update method here
            msgField.setText("");
            if (message.equalsIgnoreCase("BYE") || message.equalsIgnoreCase("logout")) {
                System.exit(0);
            }
        }
    }

    public void sendPrivateMessage(String recipient, String message) {
        String fullMessage = Controller.user.getFullName() + ":" + recipient + ":" + message;
        System.out.println("full message " + fullMessage);
        update(Controller.user.getFullName(), fullMessage); // Use the update method here
        // send messsage to the server
        writer.println(fullMessage);
        //Add message in DB
        Message m = new Message();
        m.setContent(message);
        m.setSender(Controller.user);
        String finalRecipient = recipient.substring(1);
        User receiver = Controller.users.stream().filter(u -> u.getFullName().equals(finalRecipient)).findFirst().orElse(null);
        m.setReceiver(receiver);

        try {
            ServerConnector.getControler().addMessage(m);
            for (Message mes: ServerConnector.getControler().getAllMessages(Controller.user,Controller.user)) {
                System.out.println(mes.getContent());
            };
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


    public boolean updateUsersList(ArrayList<String> clientList) {
        System.out.println("called updateUsersList");
        Platform.runLater(() -> {
            // Clear only user boxes, starting from index 2 (search box and separator)
            ObservableList items = listView.getItems();
            items.subList(0, items.size()).clear();

            // Add user boxes dynamically
            double layoutY = 0;
            for (String client : clientList) {
                if (Controller.user.getFullName().equals(client)) continue;

                HBox userBox = createUserBox(client);
                assert userBox != null;
                userBox.setLayoutY(layoutY);
                layoutY += 100; // Increment layoutY by 100 for the next user box

                // Add the user box to the list view
                listView.getItems().add(userBox);
                System.out.println("Client added to the list view");
            }
        });

        return true;
    }

    private HBox createUserBox(String client) {

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
            userBoxController.setUsername(client);

            // You can now return this HBox and use it as needed
            return userBox;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    // handle testUI button click event
    public void handleTestUI(MouseEvent event) {
        updateUsersList(Controller.users.stream().map(User::getFullName).collect(Collectors.toCollection(ArrayList::new)));
    }


    // handle list view click event
    @FXML
    public void handleListViewClick(MouseEvent event) {
        // we click on the list view that contains HBoxes of users
        // we need to get the HBox that we clicked on
        HBox userBox = (HBox) listView.getSelectionModel().getSelectedItem();
        if (userBox == null) return;
        else
        {
            // get the username from the HBox that contains image view and Vbox
            String username = ((Label) ((VBox) userBox.getChildren().get(1)).getChildren().get(0)).getText();
            System.out.println("Clicked on " + username);
        }
    }
}





