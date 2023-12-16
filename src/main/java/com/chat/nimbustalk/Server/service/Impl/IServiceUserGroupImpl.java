package com.chat.nimbustalk.Server.service.Impl;

import com.chat.nimbustalk.Server.dao.Impl.UserGroupDaoImpl;
import com.chat.nimbustalk.Server.dao.entities.Group;
import com.chat.nimbustalk.Server.dao.entities.User;
import com.chat.nimbustalk.Server.dao.entities.UserGroup;
import com.chat.nimbustalk.Server.service.IUserGroupService;

import java.util.List;

public class IServiceUserGroupImpl implements IUserGroupService {

    UserGroupDaoImpl userGroup;

    public IServiceUserGroupImpl(UserGroupDaoImpl userGroup) {
        this.userGroup = userGroup;
    }

    @Override
    public void addUserToGroup(UserGroup u) {
        this.userGroup.save(u);
    }

    @Override
    public List<Group> getGroupsByUser(User user) {
        return this.userGroup.getGroupsByUserId(user);
    }

    @Override
    public List<User> getUsersByGroup(Group group) {
        return this.userGroup.getUsersByGroupId(group);
    }
}
