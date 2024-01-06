package com.chat.nimbustalk.Server.rmi;

import com.chat.nimbustalk.Server.dao.entities.Group;
import com.chat.nimbustalk.Server.dao.entities.Message;
import com.chat.nimbustalk.Server.dao.entities.User;
import com.chat.nimbustalk.Server.dao.entities.UserGroup;
import com.chat.nimbustalk.Server.dao.entities.UserImages;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatController extends Remote {

    //Message Functions
    void addMessage(Message m) throws RemoteException;
    List<Message> getAllMessages() throws RemoteException;
    Message getMessageById(Integer id) throws RemoteException;
    List<Message> getAllMessages(User sender, User receiver) throws RemoteException;

    //User Functions
    void addUser(User u) throws RemoteException;
    List<User> getAllUsers() throws RemoteException;
    User getUserById(Integer id) throws RemoteException;
    User getUserByUsername(String username) throws RemoteException;

    //Group Functions
    void addGroup(Group group) throws RemoteException;
    List<Group> getAllGroups() throws RemoteException;
    Group getGroupById(Integer id) throws RemoteException;
    Group getGroupByName(String name) throws RemoteException;

    //UserGroup Functions
    void addUserToGroup(UserGroup u) throws RemoteException;
    List<Group> getGroupsByUser(User user) throws RemoteException;
    List<User> getUsersByGroup(Group group) throws RemoteException;

    // UserImages Functions
    void addUserImage(UserImages u) throws RemoteException;
    List<UserImages> getAllUserImages() throws RemoteException;
    UserImages getUserImageById(Integer id) throws RemoteException;
    UserImages getUserImageByUserId(Integer id) throws RemoteException;

    // delete user image
   void deleteByUserId(Integer id) throws RemoteException;

}