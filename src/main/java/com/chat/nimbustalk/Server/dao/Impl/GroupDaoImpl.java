package com.chat.nimbustalk.Server.dao.Impl;

import com.chat.nimbustalk.Server.dao.DBConnection;
import com.chat.nimbustalk.Server.dao.GroupDao;
import com.chat.nimbustalk.Server.dao.entities.Group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GroupDaoImpl implements GroupDao {
    @Override
    public void save(Group group) {
        try {
            PreparedStatement pstm = DBConnection.getConnection().prepareStatement("Insert into groupe (name) values (?)");
            pstm.setString(1,group.getName());
            pstm.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Group getById(Integer id) {
        Group group = new Group();
        try {
            PreparedStatement pstm = DBConnection.getConnection().prepareStatement("Select * from groupe where id = ?");
            pstm.setInt(1,id);
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                group.setId(rs.getInt(1));
                group.setName(rs.getString(2));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return group;
    }

    @Override
    public List<Group> getAll() {
        ArrayList<Group> groups = new ArrayList<>();
        try {
            Statement stm = DBConnection.getConnection().createStatement();
            ResultSet rs = stm.executeQuery("Select * from groupe");
            while(rs.next()){
                Group group= new Group();
                group.setId(rs.getInt(1));
                group.setName(rs.getString(2));
                groups.add(group);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return groups;
    }
}
