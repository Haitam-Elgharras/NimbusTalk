package com.chat.nimbustalk.Server.dao.Impl;

import com.chat.nimbustalk.Server.dao.UserImagesDao;
import com.chat.nimbustalk.Server.dao.DBConnection;
import com.chat.nimbustalk.Server.dao.entities.UserImages;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserImagesDaoImpl implements UserImagesDao {

    @Override
    public void save(UserImages o) {
        String sql = "INSERT INTO user_images (user_id, image) VALUES (?, ?)";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, o.getUserId());
            statement.setBytes(2, o.getImage());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserImages getById(Integer id) {
        UserImages userImages = new UserImages();
        String sql = "SELECT * FROM user_images WHERE id = ?";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userImages.setId(resultSet.getInt("id"));
                userImages.setUserId(resultSet.getInt("user_id"));
                userImages.setImage(resultSet.getBytes("image"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userImages;
    }

    @Override
    public List<UserImages> getAll() {
        List<UserImages> userImagesList = new ArrayList<>();
        String sql = "SELECT * FROM user_images";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UserImages userImages = new UserImages();
                userImages.setId(resultSet.getInt("id"));
                userImages.setUserId(resultSet.getInt("user_id"));
                userImages.setImage(resultSet.getBytes("image"));
                userImagesList.add(userImages);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userImagesList;
    }

    @Override
    public UserImages getUserImageByUserId(int userId) {
        UserImages userImages = new UserImages();
        String sql = "SELECT * FROM user_images WHERE user_id = ?";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userImages.setId(resultSet.getInt("id"));
                userImages.setUserId(resultSet.getInt("user_id"));
                userImages.setImage(resultSet.getBytes("image"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userImages;
    }

    public void deleteByUserId(int userId) {
        String sql = "DELETE FROM user_images WHERE user_id = ?";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}