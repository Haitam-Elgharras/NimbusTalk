package com.chat.nimbustalk.Server.service.Impl;

import com.chat.nimbustalk.Server.dao.Impl.UserImagesDaoImpl;
import com.chat.nimbustalk.Server.dao.entities.UserImages;
import com.chat.nimbustalk.Server.service.IServiceUserImages;

import java.util.List;

public class IServiceUserImagesImpl implements IServiceUserImages {
    UserImagesDaoImpl userImagesDao;

    public IServiceUserImagesImpl(UserImagesDaoImpl userImagesDao) {
        this.userImagesDao = userImagesDao;
    }

    @Override
    public void addUserImage(UserImages u) {
        this.userImagesDao.save(u);
    }

    @Override
    public List<UserImages> getAllUserImages() {
        return this.userImagesDao.getAll();
    }

    @Override
    public UserImages getUserImageById(Integer id) {
        return this.userImagesDao.getById(id);
    }

    @Override
    public UserImages getUserImageByUserId(Integer userId) {
        return this.userImagesDao.getUserImageByUserId(userId);
    }

    public void deleteByUserId(Integer userId) {
        this.userImagesDao.deleteByUserId(userId);
    }
}