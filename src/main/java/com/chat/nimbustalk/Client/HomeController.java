package com.chat.nimbustalk.Client;



import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.scene.text.Font;
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
            writer.println(Controller.username);

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
                if (cmd.equalsIgnoreCase(Controller.username + ":")) {
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

        if (username.equalsIgnoreCase(Controller.username) && tokens.length > 1 && tokens[1].startsWith("@")) {
            tokens[0] = "You to " + tokens[1].substring(1) + " :";
            tokens[1] ="";
            message = String.join(" ", tokens);
        }
        else
        if (username.equalsIgnoreCase(Controller.username)) {
            tokens[0] = "You :";
            message = String.join(" ", tokens);
        }

        Text text = new Text(message);

        text.setFill(Color.WHITE);
        text.getStyleClass().add("message");
        TextFlow tempFlow = new TextFlow();
        if (!Controller.username.equals(username)) {
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

        if (!Controller.username.equals(username)) {
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
            int spaceIndex = message.indexOf(" ");
            if (spaceIndex != -1) {
                recipient = message.substring(0, spaceIndex);
                message = message.substring(spaceIndex + 1);
            }
        }

        if (recipient != null && !recipient.isEmpty()) {
            sendPrivateMessage(recipient, message);
        } else {
            // Regular public message
            String fullMessage = Controller.username + ": " + message;
            writer.println(fullMessage);
            update(Controller.username, fullMessage); // Use the update method here
            msgField.setText("");
            if (message.equalsIgnoreCase("BYE") || message.equalsIgnoreCase("logout")) {
                System.exit(0);
            }
        }
    }

    public void sendPrivateMessage(String recipient, String message) {
        String fullMessage = Controller.username + ":" + recipient + ":" + message;
        writer.println(fullMessage);
        update(Controller.username, fullMessage); // Use the update method here
        // add to a database
        msgField.setText("");
        if (message.equalsIgnoreCase("BYE") || message.equalsIgnoreCase("logout")) {
            System.exit(0);
        }
    }

    public void handleUserBoxClick(MouseEvent event) throws MalformedURLException {
    }


    @FXML
    void emojiAction(MouseEvent event) {
        emojiList.setVisible(!emojiList.isVisible());
    }


    public boolean updateUI(ArrayList<String> clientList) {
        Platform.runLater(() -> {
            // Clear only user boxes, starting from index 2 (search box and separator)
            ObservableList<Node> children = clientListBox.getChildren();
            children.subList(2, children.size()).clear();

            // Add user boxes dynamically
            double layoutY = 0;
            for (String client : clientList) {
                if (Controller.username.equals(client)) continue;

                HBox userBox = createUserBox(client, layoutY);
                layoutY += 100; // Increment layoutY by 100 for the next user box

                clientListBox.getChildren().add(userBox);
                System.out.println("User box added");
            }
        });

        return true;
    }

    private HBox createUserBox(String client, double layoutY) {
        HBox userBox = new HBox();
        userBox.getStyleClass().addAll("dark-gray-background", "user-box");
        userBox.setPrefWidth(315);
        userBox.setPrefHeight(100);
        userBox.setLayoutX(0);
        userBox.setLayoutY(layoutY);

        ImageView userProfileImage = createImageView("icons/userConv.png", 60.0, 69.0);
        VBox userDetails = createUserDetails(client);

        userBox.getChildren().addAll(userProfileImage, userDetails, new Pane(), createCountCircle(14.0, Color.rgb(80, 201, 132), "1", 15.0));
        HBox.setMargin(userProfileImage, new Insets(0, 0, 0, 20));
        HBox.setMargin(userDetails, new Insets(0, 0, 0, 10));

        return userBox;
    }

    private VBox createUserDetails(String client) {
        VBox userDetails = new VBox();
        userDetails.setPrefWidth(160);
        userDetails.setPrefHeight(79);
        userDetails.setLayoutX(0);

        Label usernameLabel = createUsernameLabel(client);
        Label lastMessageLabel = createLastMessageLabel();

        userDetails.getChildren().addAll(usernameLabel, lastMessageLabel);
        return userDetails;
    }

    private Label createUsernameLabel(String client) {
        Label usernameLabel = new Label(client);
        usernameLabel.setStyle("-fx-text-fill: WHITE;");
        usernameLabel.setFont(new Font("Ebrima Bold", 20.0));
        usernameLabel.setPrefWidth(148);
        usernameLabel.setPrefHeight(32);
        usernameLabel.setLayoutX(0);
        usernameLabel.setLayoutY(18);

        return usernameLabel;
    }

    private Label createLastMessageLabel() {
        Label lastMessageLabel = new Label("You: see u");
        lastMessageLabel.setStyle("-fx-text-fill: #9da7a7;");
        lastMessageLabel.setFont(new Font("Ebrima", 18.0));
        lastMessageLabel.setPrefWidth(193);
        lastMessageLabel.setPrefHeight(32);
        lastMessageLabel.setLayoutX(10);
        lastMessageLabel.setLayoutY(50);

        return lastMessageLabel;
    }

    private Pane createCountCircle(double radius, Color fill, String labelText, double labelFontSize) {
        Pane countCircle = new Pane();
        countCircle.setPrefWidth(101);
        countCircle.setPrefHeight(79);
        countCircle.setLayoutX(0);

        Circle circle = new Circle(radius);
        circle.setFill(fill);

        Label countLabel = new Label(labelText);
        countLabel.setStyle("-fx-text-fill: WHITE; -fx-font-size: " + labelFontSize + ";");

        countCircle.getChildren().addAll(circle, countLabel);
        return countCircle;
    }

    private ImageView createImageView(String imagePath, double fitHeight, double fitWidth) {
        File file = new File("src/main/java/com/chat/nimbustalk/" + imagePath);
        ImageView imageView = new ImageView(new Image(file.toURI().toString()));
        imageView.setFitHeight(fitHeight);
        imageView.setFitWidth(fitWidth);
        imageView.setLayoutX(0);
        imageView.setLayoutY(0);

        return imageView;
    }




    // handle testUI button click event
    public void handleTestUI(MouseEvent event) {
        updateUI(Controller.users.stream().map(u -> u.name).collect(Collectors.toCollection(ArrayList::new)));
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





