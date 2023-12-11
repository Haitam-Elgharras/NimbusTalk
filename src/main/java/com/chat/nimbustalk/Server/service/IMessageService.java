package com.chat.nimbustalk.Server.service;

import com.chat.nimbustalk.Server.dao.entities.Message;
import java.util.List;

public interface IMessageService {
    void addMessage(Message m);
    List<Message> getAllProducts();
    Message getProductById(Integer id);
}
