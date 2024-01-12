package com.chat.nimbustalk.Server;

import com.chat.nimbustalk.Client.HomeController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {

    private ArrayList<ClientHandler> clients;
    public int userId;
    public String username;
    private Socket socket;
    private BufferedReader reader;
    PrintWriter writer;

    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients, String username) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.username = username;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String msg;
            while ((msg = reader.readLine()) != null) {
                if (msg.equalsIgnoreCase("exit"))
                    break;

                if(msg.equalsIgnoreCase("updateListOfUsers")) {
                    System.out.println("this is the update list of users from the client handler");
                    for (ClientHandler cl : clients) {
                        cl.writer.println("updateListOfUsers");
                    }
                    continue;
                }

                // Split the message into tokens
                String[] tokens = msg.split(":");
                String recipientFullname = tokens[0];
                System.out.println("sender fullname: " + recipientFullname);
                String recipient = null;
                String actualMessage = null;

                // Check if it's a private message
                if (tokens.length > 1 && tokens[1].startsWith("@")) {
                    recipient = tokens[1].substring(1);
                    
                    
                    actualMessage = msg.substring(recipientFullname.length() + recipient.length() + 3); //for : : and @
                    
                } else {
                    // Regular public message
                    // useless code !!!
                    actualMessage = msg.substring(recipientFullname.length() + 1);
                }

                // Handle the message based on whether it's private or public
                if (recipient != null && !recipient.isEmpty()) {
                    
                    // It's a private message, find the appropriate client and send the message
                    for (ClientHandler cl : clients) {
                        if (cl.username.equals(recipient)) {
                            cl.writer.println(recipientFullname + " (private): " + actualMessage);
                            
                            break;
                        }
                    }
                } else {
                        // Regular public message, broadcast to all clients
                        for (ClientHandler cl : clients)
                            cl.writer.println("public:"+msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
                reader.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

