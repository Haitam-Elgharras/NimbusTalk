package com.chat.nimbustalk.Server.service;

import com.chat.nimbustalk.Server.dao.entities.User;

import java.util.List;

public interface IUserService {
    void addUser(User u);
    List<User> getAllUsers();
    User getUserById(Integer id);
    User getUserByUsername(String username);
}
