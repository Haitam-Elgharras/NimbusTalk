package com.chat.nimbustalk.Server.service.Impl;

import com.chat.nimbustalk.Server.dao.Impl.UserDaoImpl;
import com.chat.nimbustalk.Server.dao.entities.User;
import com.chat.nimbustalk.Server.service.IUserService;

import java.util.List;

public class IServiceUserImpl implements IUserService {
    UserDaoImpl userDao;

    public IServiceUserImpl(UserDaoImpl userDao) {
        this.userDao = userDao ;
    }
    @Override
    public void addUser(User c) {
        this.userDao.save(c);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userDao.getAll();
    }

    @Override
    public User getUserById(Integer id) {
        return this.userDao.getById(id);
    }

}
