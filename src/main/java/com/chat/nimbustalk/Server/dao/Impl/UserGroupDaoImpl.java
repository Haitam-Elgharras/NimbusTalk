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
            PreparedStatement pstm = DBConnection.getConnection()
                    .prepareStatement("Insert into user_groups (user_id, group_id) values (?,?)");
            pstm.setInt(1,o.getUser().getId());
            pstm.setInt(2,o.getGroup().getId());
            pstm.executeUpdate();
        } catch (Exception e){
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
                groups.add(groupDao.getById(rs.getInt(2)));
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
                users.add(userDao.getById(rs.getInt(1)));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return users;
    }
}
