package com.chat.nimbustalk.Server;

import com.chat.nimbustalk.Server.dao.Impl.MessageDaoImpl;
import com.chat.nimbustalk.Server.dao.Impl.UserDaoImpl;
import com.chat.nimbustalk.Server.rmi.ChatControllerImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

public class Server {
    private static final ArrayList<ClientHandler> clients = new ArrayList<>();
    public static void main(String[] args) {

        try {
            ChatControllerImpl c = new ChatControllerImpl(new MessageDaoImpl(), new UserDaoImpl());
            System.out.println(c.getAllUsers().size());
            LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost/1099/ob", c);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

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

                // Create a new ClientHandler instance with the username
                ClientHandler clientThread = new ClientHandler(socket, clients, username);
                clients.add(clientThread);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
