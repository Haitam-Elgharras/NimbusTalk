package com.chat.nimbustalk.Client.connector;

import com.chat.nimbustalk.Server.rmi.ChatController;

import java.rmi.Naming;

public class ServerConnector {
    private static ServerConnector serverConnector;
    private static ChatController chatController;
    private ServerConnector(){
        try {
            chatController = (ChatController) Naming.lookup("rmi://localhost/1099/ob");
            System.out.println(chatController);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ChatController getController(){
        if (serverConnector == null) serverConnector = new ServerConnector();
        return chatController;
    }

    public static void setController(ChatController controller) {
        chatController = controller;
    }
}
