package com.chat.nimbustalk.Server.dao.Impl;

import com.chat.nimbustalk.Server.dao.DBConnection;
import com.chat.nimbustalk.Server.dao.UserGroupDao;
import com.chat.nimbustalk.Server.dao.entities.Group;
import com.chat.nimbustalk.Server.dao.entities.User;
import com.chat.nimbustalk.Server.dao.entities.UserGroup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserGroupDaoImpl implements UserGroupDao {
    @Override
    public void save(UserGroup o) {
        try {
            // Check if the user exists
            PreparedStatement checkUserStmt = DBConnection.getConnection()
                    .prepareStatement("SELECT COUNT(*) FROM User WHERE id = ?");
            checkUserStmt.setInt(1, o.getUser().getId());
            ResultSet rs = checkUserStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // The user exists, insert the row into the user_groups table
                PreparedStatement pstm = DBConnection.getConnection()
                        .prepareStatement("INSERT INTO user_groups (user_id, group_id) VALUES (?, ?)");
                pstm.setInt(1, o.getUser().getId());
                pstm.setInt(2, o.getGroup().getId());
                pstm.executeUpdate();
            } else {
                // The user does not exist, handle the error appropriately
                System.out.println("Error: User with ID " + o.getUser().getId() + " does not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserGroup getById(Integer id) {
        return null;
    }

    @Override
    public List<UserGroup> getAll() {
        return null;
    }

    @Override
    public List<Group> getGroupsByUserId(User user) {
        ArrayList<Group> groups = new ArrayList<>();
        GroupDaoImpl groupDao = new GroupDaoImpl();
        try {
            PreparedStatement pstm = DBConnection.getConnection().prepareStatement("Select * from user_groups where user_id = ?");
            pstm.setInt(1,user.getId());
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                groups.add(groupDao.getById(rs.getInt("group_id")));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return groups;
    }

    @Override
    public List<User> getUsersByGroupId(Group group) {
        ArrayList<User> users = new ArrayList<>();
        UserDaoImpl userDao = new UserDaoImpl();
        try {
            PreparedStatement pstm = DBConnection.getConnection().prepareStatement("Select * from user_groups where group_id = ?");
            pstm.setInt(1,group.getId());
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                users.add(userDao.getById(rs.getInt("user_id")));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return users;
    }
}
