package com.chat.nimbustalk.Server.service.Impl;

import com.chat.nimbustalk.Server.dao.Impl.MessageDaoImpl;
import com.chat.nimbustalk.Server.dao.entities.Group;
import com.chat.nimbustalk.Server.dao.entities.Message;
import com.chat.nimbustalk.Server.dao.entities.User;
import com.chat.nimbustalk.Server.service.IMessageService;
import java.util.List;

public class IServiceMessageImpl implements IMessageService {
    MessageDaoImpl messageDao;
    public IServiceMessageImpl(MessageDaoImpl messageDao) {
        this.messageDao = messageDao;
    }
    @Override
    public void addMessage(Message m) {
        this.messageDao.save(m);
    }
    @Override
    public List<Message> getAllMessages() {
        return this.messageDao.getAll();
    }
    @Override
    public Message getMessageById(Integer id) {return this.messageDao.getById(id);}

    @Override
    public List<Message> getAllMessages(User sender, User receiver) {
        return this.messageDao.getAll(sender,receiver);
    }

    @Override
    public List<Message> getAllMessages(Group group) {return this.messageDao.getAll(group);}

}
