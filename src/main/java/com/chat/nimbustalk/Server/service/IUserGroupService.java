package com.chat.nimbustalk.Server.service;

import com.chat.nimbustalk.Server.dao.entities.Group;
import com.chat.nimbustalk.Server.dao.entities.User;
import com.chat.nimbustalk.Server.dao.entities.UserGroup;

import java.util.List;

public interface IUserGroupService {
    void addUserToGroup(UserGroup u);
    List<Group> getGroupsByUser(User user);
    List<User> getUsersByGroup(Group group);
}
