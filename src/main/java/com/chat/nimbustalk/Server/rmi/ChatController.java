package com.chat.nimbustalk.Server.rmi;

import com.chat.nimbustalk.Server.dao.entities.Message;
import com.chat.nimbustalk.Server.dao.entities.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatController extends Remote {

    void addMessage(Message m) throws RemoteException;
    List<Message> getAllMessages() throws RemoteException;
    Message getMessageById(Integer id) throws RemoteException;

    void addUser(User u) throws RemoteException;
    List<User> getAllUsers() throws RemoteException;
    User getUserById(Integer id) throws RemoteException;

    List<Message> getAllMessages(User sender, User receiver) throws RemoteException;
}