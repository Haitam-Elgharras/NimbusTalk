package com.chat.nimbustalk.Server.service;

import com.chat.nimbustalk.Server.dao.entities.UserImages;

import java.util.List;

public interface IServiceUserImages {
    void addUserImage(UserImages u);
    List<UserImages> getAllUserImages();
    UserImages getUserImageById(Integer id);
    UserImages getUserImageByUserId(Integer userId);

}