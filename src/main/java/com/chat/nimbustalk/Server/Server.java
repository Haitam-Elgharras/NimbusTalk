package com.chat.nimbustalk.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final ArrayList<ClientHandler> clients = new ArrayList<>();
    private static final ArrayList<String> users = new ArrayList<>();
    public static void main(String[] args) {
        Socket socket;
        try(
            ServerSocket serverSocket = new ServerSocket(8889)
            )
        {
            while(true) {
                System.out.println("Waiting for clients...");
                socket = serverSocket.accept(); // the method blocks until a client connects
                System.out.println("Connected");

                // Read the username from the client
                // TODO: move this to the ClientHandler class cause it's blocking code
                BufferedReader usernameReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String username = usernameReader.readLine();
                System.out.println("from the server " + username);
                users.add(username);

                // Create a new ClientHandler instance with the username
                ClientHandler clientThread = new ClientHandler(socket, clients, username, users);
                clients.add(clientThread);
                clientThread.start();

                // if a new client is bieng add send a message to all clients to update the list
                for (ClientHandler cl : clients) {
                    cl.writer.println("updateListOfUsers");
                    System.out.println("updateListOfUsers" + cl.username);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
