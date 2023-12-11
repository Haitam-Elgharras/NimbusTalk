package com.chat.nimbustalk.Server.service.Impl;

import com.chat.nimbustalk.Server.dao.Impl.MessageDaoImpl;
import com.chat.nimbustalk.Server.dao.entities.Message;
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
    public List<Message> getAllProducts() {
        return this.messageDao.getAll();
    }
    @Override
    public Message getProductById(Integer id) {return this.messageDao.getById(id);}

    }
