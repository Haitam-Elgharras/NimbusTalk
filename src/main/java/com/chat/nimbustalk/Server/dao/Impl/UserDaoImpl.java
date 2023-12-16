package com.chat.nimbustalk.Server.dao.Impl;

import com.chat.nimbustalk.Server.dao.UserDao;
import com.chat.nimbustalk.Server.dao.DBConnection;
import com.chat.nimbustalk.Server.dao.entities.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    @Override
    public void save(User o) {
        try {
            PreparedStatement pstm = DBConnection.getConnection()
                    .prepareStatement("Insert into User (fullName,email,password,gender,phoneNumber) values (?,?,?,?,?)");
            pstm.setString(1,o.getFullName());
            pstm.setString(2,o.getEmail());
            pstm.setString(3,o.getPassword());
            pstm.setString(4,o.getGender());
            pstm.setString(5,o.getPhoneNumber());
            pstm.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public User getById(Integer id) {
        User c = new User();
        try {
            PreparedStatement pstm = DBConnection.getConnection().prepareStatement("Select * from User where id = ?");
            pstm.setInt(1,id);
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                c.setId(rs.getInt(1));
                c.setFullName(rs.getString(2));
                c.setEmail(rs.getString(3));
                c.setPassword(rs.getString(4));
                c.setGender(rs.getString(5));
                c.setPhoneNumber(rs.getString(6));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public List<User> getAll() {
        ArrayList<User> clients = new ArrayList<>();
        try {
            Statement stm = DBConnection.getConnection().createStatement();
            ResultSet rs = stm.executeQuery("Select * from User");
            while(rs.next()){
                User c= new User();
                c.setId(rs.getInt(1));
                c.setFullName(rs.getString(2));
                c.setEmail(rs.getString(3));
                c.setPassword(rs.getString(4));
                c.setGender(rs.getString(5));
                c.setPhoneNumber(rs.getString(6));
                clients.add(c);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public User getUserByUsername(String username) {
        User c = new User();
        try {
            PreparedStatement pstm = DBConnection.getConnection().prepareStatement("Select * from User where fullName = ?");
            pstm.setString(1,username);
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                c.setId(rs.getInt(1));
                c.setFullName(rs.getString(2));
                c.setEmail(rs.getString(3));
                c.setPassword(rs.getString(4));
                c.setGender(rs.getString(5));
                c.setPhoneNumber(rs.getString(6));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return c;
    }
}
