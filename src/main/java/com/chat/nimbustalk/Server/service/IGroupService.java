package com.chat.nimbustalk.Server.service;

import com.chat.nimbustalk.Server.dao.entities.Group;

import java.util.List;

public interface IGroupService {
    void addGroup(Group group);
    List<Group> getAllGroups();
    Group getGroupById(Integer id);
}
