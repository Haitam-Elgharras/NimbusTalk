package com.chat.nimbustalk.Server.service;

import com.chat.nimbustalk.Server.dao.entities.Message;
import com.chat.nimbustalk.Server.dao.entities.User;

import java.util.List;

public interface IMessageService {
    void addMessage(Message m);
    List<Message> getAllMessages();
    Message getMessageById(Integer id);
    List<Message> getAllMessages(User sender, User receiver);
}
