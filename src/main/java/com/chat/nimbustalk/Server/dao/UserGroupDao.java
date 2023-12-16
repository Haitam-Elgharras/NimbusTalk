package com.chat.nimbustalk.Server.dao;

import com.chat.nimbustalk.Server.dao.entities.Group;
import com.chat.nimbustalk.Server.dao.entities.User;
import com.chat.nimbustalk.Server.dao.entities.UserGroup;

import java.util.List;

public interface UserGroupDao extends DAO<UserGroup,Integer> {
    List<Group> getGroupsByUserId(User user);
    List<User> getUsersByGroupId(Group group);
}
