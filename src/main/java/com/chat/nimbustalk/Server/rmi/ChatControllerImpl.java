package com.chat.nimbustalk.Server.rmi;

import com.chat.nimbustalk.Server.dao.Impl.MessageDaoImpl;
import com.chat.nimbustalk.Server.dao.Impl.UserDaoImpl;
import com.chat.nimbustalk.Server.dao.entities.Group;
import com.chat.nimbustalk.Server.dao.entities.Message;
import com.chat.nimbustalk.Server.dao.entities.User;
import com.chat.nimbustalk.Server.dao.entities.UserGroup;
import com.chat.nimbustalk.Server.service.Impl.IServiceGroupImpl;
import com.chat.nimbustalk.Server.service.Impl.IServiceMessageImpl;
import com.chat.nimbustalk.Server.service.Impl.IServiceUserGroupImpl;
import com.chat.nimbustalk.Server.service.Impl.IServiceUserImpl;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ChatControllerImpl extends UnicastRemoteObject implements ChatController {
    private IServiceMessageImpl serviceMessage;
    private IServiceUserImpl serviceUser;
    private IServiceGroupImpl serviceGroup;

    private IServiceUserGroupImpl serviceUserGroup;

    public ChatControllerImpl(IServiceUserImpl serviceUser, IServiceMessageImpl serviceMessage, IServiceGroupImpl serviceGroup, IServiceUserGroupImpl serviceUserGroup) throws RemoteException {
        super();
        this.serviceUser = serviceUser;
        this.serviceMessage = serviceMessage;
        this.serviceGroup = serviceGroup;
        this.serviceUserGroup = serviceUserGroup;
    }
    @Override
    public void addMessage(Message m) throws RemoteException {
        this.serviceMessage.addMessage(m);
    }
    @Override
    public List<Message> getAllMessages() throws RemoteException {
        return this.serviceMessage.getAllMessages();
    }
    @Override
    public Message getMessageById(Integer id) throws RemoteException {
        return this.serviceMessage.getMessageById(id);
    }

    @Override
    public void addUser(User c) throws RemoteException {
        this.serviceUser.addUser(c);
    }

    @Override
    public List<User> getAllUsers() throws RemoteException {
        return this.serviceUser.getAllUsers();
    }

    @Override
    public User getUserById(Integer id) throws RemoteException {
        return this.serviceUser.getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) throws RemoteException {
        return this.serviceUser.getUserByUsername(username);
    }

    @Override
    public List<Message> getAllMessages(User sender, User receiver) throws RemoteException {
        return this.serviceMessage.getAllMessages(sender,receiver);
    }

    @Override
    public List<Message> getAllMessages(Group group) throws RemoteException {
        return this.serviceMessage.getAllMessages(group);
    }

    @Override
    public void addGroup(Group group) throws RemoteException {
        this.serviceGroup.addGroup(group);
    }

    @Override
    public List<Group> getAllGroups() throws RemoteException {
        return this.serviceGroup.getAllGroups();
    }

    @Override
    public Group getGroupById(Integer id) throws RemoteException {
        return this.serviceGroup.getGroupById(id);
    }

    @Override
    public Group getGroupByName(String name) throws RemoteException {
        return this.serviceGroup.getGroupByName(name);
    }


    @Override
    public void addUserToGroup(UserGroup u) throws RemoteException {
        this.serviceUserGroup.addUserToGroup(u);
    }

    @Override
    public List<Group> getGroupsByUser(User user) throws RemoteException {
        return this.serviceUserGroup.getGroupsByUser(user);
    }

    @Override
    public List<User> getUsersByGroup(Group group) throws RemoteException {
        return this.serviceUserGroup.getUsersByGroup(group);
    }

}
