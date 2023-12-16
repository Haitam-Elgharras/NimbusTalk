package com.chat.nimbustalk.Server.service;

import com.chat.nimbustalk.Server.dao.entities.Group;
import com.chat.nimbustalk.Server.dao.entities.User;

import java.util.List;

public interface IGroupService {
    void addGroup(Group group);
    List<Group> getAllGroups();
    Group getGroupById(Integer id);
    Group getGroupByName(String name);
}
