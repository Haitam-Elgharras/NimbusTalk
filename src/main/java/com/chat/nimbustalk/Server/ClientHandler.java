package com.chat.nimbustalk.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {

    private ArrayList<ClientHandler> clients;
    public String username;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

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

                // Split the message into tokens
                String[] tokens = msg.split(":");
                String sender = tokens[0];
                String recipient = null;
                String actualMessage = null;

                // Check if it's a private message
                if (tokens.length > 1 && tokens[1].startsWith("@")) {
                    recipient = tokens[1].substring(1);
                    actualMessage = msg.substring(sender.length() + recipient.length() + 3); //for : : and @
                    System.out.println("actual message " + actualMessage);
                } else {
                    // Regular public message
                    actualMessage = msg.substring(sender.length() + 1);
                }

                // Handle the message based on whether it's private or public
                if (recipient != null && !recipient.isEmpty()) {
                    System.out.println("private message " + msg);
                    // It's a private message, find the appropriate client and send the message
                    for (ClientHandler cl : clients) {
                        if (cl.username.equalsIgnoreCase(recipient)) {
                            cl.writer.println(sender + " (private): " + actualMessage);
                            break;
                        }
                    }
                } else {
                    {
                        System.out.println("public message " + msg);
                        // Regular public message, broadcast to all clients
                        for (ClientHandler cl : clients)
                            cl.writer.println(msg);
                    }

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

