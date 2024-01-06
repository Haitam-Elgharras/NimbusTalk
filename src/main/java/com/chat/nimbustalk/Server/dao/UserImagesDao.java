package com.chat.nimbustalk.Server.dao;

import com.chat.nimbustalk.Server.dao.entities.UserImages;

public interface UserImagesDao extends DAO<UserImages, Integer> {
    UserImages getUserImageByUserId(int userId);
}