package com.chat.nimbustalk.Server;

import com.chat.nimbustalk.Server.dao.Impl.*;
import com.chat.nimbustalk.Server.rmi.ChatControllerImpl;
import com.chat.nimbustalk.Server.service.Impl.*;

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
    private static final ArrayList<String> users = new ArrayList<>();
    public static void main(String[] args) {

        try {
            ChatControllerImpl c = new ChatControllerImpl(new IServiceUserImpl(new UserDaoImpl()), new IServiceMessageImpl(new MessageDaoImpl()), new IServiceGroupImpl(new GroupDaoImpl()), new IServiceUserGroupImpl(new UserGroupDaoImpl()),new IServiceUserImagesImpl(new UserImagesDaoImpl()));
            LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost/1099/ob", c);
        }
        catch (Exception e) {
            
        }

        Socket socket;
        try(
            ServerSocket serverSocket = new ServerSocket(8889)
            )
        {
            while(true) {
                
                socket = serverSocket.accept(); // the method blocks until a client connects
                

                // Read the username from the client
                // TODO 1: move this to the ClientHandler class cause it's blocking code
                // TODO 2: we shouldn't depend on the username instead we should do on the id
                //  look at the problem when the name is duplicated or the name has two parts
//                BufferedReader usernameReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                String username = usernameReader.readLine();
//                
//                users.add(username);
                // we will read the user id from the client
                BufferedReader usernameReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String username = usernameReader.readLine();
                

                // Create a new ClientHandler instance with the username
                ClientHandler clientThread = new ClientHandler(socket, clients, username);
                clients.add(clientThread);
                clientThread.start();

                // if a new client is being add send a message to all clients to update the list
                for (ClientHandler cl : clients) {
                    cl.writer.println("updateListOfUsers");
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
