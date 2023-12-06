package com.chat.nimbustalk.Client;



import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

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
    public TextArea msgRoom;// where the messages are displayed
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
    private FileChooser fileChooser;
    private File filePath;
    public boolean toggleChat = false, toggleProfile = false;

    BufferedReader reader; // to read the messages from the server
    PrintWriter writer; // to write the messages to the server
    Socket socket;  // to connect with the server


    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
                StringBuilder fulmsg = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fulmsg.append(tokens[i]);
                }

                // 4. Print the command and the full message
                System.out.println(cmd);
                System.out.println(fulmsg);

                // 5. Skip messages sent by the current user cause already displayed
                if (cmd.equalsIgnoreCase(Controller.username + ":")) {
                    continue;
                } else if (fulmsg.toString().equalsIgnoreCase("bye")) {
                    // 6. If the message indicates "bye," exit the loop and close resources
                    break;
                }

                // 7. Append the message to the TextArea (msgRoom) for display
                msgRoom.appendText(msg + "\n");
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

    public void sendMessageByKey(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            send();
        }
    }

    public void handleSendEvent(MouseEvent event) {
        send();
    }
    public void send() {
        // If the message starts with "@username", it's a private message
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
            writer.println(Controller.username + ": " + message);
            msgRoom.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            msgRoom.appendText("Me: " + message + "\n");
            msgField.setText("");
            if (message.equalsIgnoreCase("BYE") || message.equalsIgnoreCase("logout")) {
                System.exit(0);
            }
        }
    }

    public void sendPrivateMessage(String recipient, String message) {
        String msg = Controller.username + ":" + recipient + ":" + message;
        writer.println(msg);
        System.out.println("from room " + msg);
        msgRoom.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        msgRoom.appendText("Me to " + recipient + ": " + message + "\n");
        msgField.setText("");
        if (message.equalsIgnoreCase("BYE") || message.equalsIgnoreCase("logout")) {
            System.exit(0);
        }
    }
}