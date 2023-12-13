package com.chat.nimbustalk.Server.service.Impl;

import com.chat.nimbustalk.Server.dao.Impl.GroupDaoImpl;
import com.chat.nimbustalk.Server.dao.entities.Group;
import com.chat.nimbustalk.Server.service.IGroupService;

import java.util.List;

public class IServiceGroupImpl implements IGroupService {
    GroupDaoImpl groupDao;

    public IServiceGroupImpl(GroupDaoImpl groupDao){this.groupDao = groupDao;}

    @Override
    public void addGroup(Group group) {
        this.groupDao.save(group);
    }

    @Override
    public List<Group> getAllGroups() {
        return this.groupDao.getAll();
    }

    @Override
    public Group getGroupById(Integer id) {
        return this.groupDao.getById(id);
    }
}
