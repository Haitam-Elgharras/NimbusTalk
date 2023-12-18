package com.chat.nimbustalk.Server.dao;

import com.chat.nimbustalk.Server.dao.entities.Group;
import com.chat.nimbustalk.Server.dao.entities.Message;
import com.chat.nimbustalk.Server.dao.entities.User;

import java.util.List;

public interface MessageDao extends DAO<Message, Integer> {
    List<Message> getAll(User sender, User receiver);
    List<Message> getAll(Group group);
}
