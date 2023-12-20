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
                    .prepareStatement("INSERT INTO User (username, fullName, email, password, gender, phoneNumber) VALUES (?, ?, ?, ?, ?, ?)");
            pstm.setString(1, o.getUsername());  // Assuming you have a getUsername method in your User class
            pstm.setString(2, o.getFullName());
            pstm.setString(3, o.getEmail());
            pstm.setString(4, o.getPassword());
            pstm.setString(5, o.getGender());
            pstm.setString(6, o.getPhoneNumber());
            pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public User getById(Integer id) {
        User c = new User();
        try {
            PreparedStatement pstm = DBConnection.getConnection()
                    .prepareStatement("Select id, fullName, email, password, gender, phoneNumber, username from User where id = ?");
            pstm.setInt(1,id);
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                c.setId(rs.getInt("id"));
                c.setFullName(rs.getString("fullName"));
                c.setEmail(rs.getString("email"));
                c.setPassword(rs.getString("password"));
                c.setGender(rs.getString("gender"));
                c.setPhoneNumber(rs.getString("phoneNumber"));
                c.setUsername(rs.getString("username"));
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
            ResultSet rs = stm.executeQuery("Select id, fullName, email, password, gender, phoneNumber, username from User");
            while(rs.next()){
                User c= new User();
                c.setId(rs.getInt("id"));
                c.setFullName(rs.getString("fullName"));
                c.setEmail(rs.getString("email"));
                c.setPassword(rs.getString("password"));
                c.setGender(rs.getString("gender"));
                c.setPhoneNumber(rs.getString("phoneNumber"));
                c.setUsername(rs.getString("username"));
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
            PreparedStatement pstm = DBConnection.getConnection().prepareStatement(
                    "Select id, fullName, email, password, gender, phoneNumber, username from User where username = ?"
            );
            pstm.setString(1,username);
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                c.setId(rs.getInt("id"));
                c.setFullName(rs.getString("fullName"));
                c.setEmail(rs.getString("email"));
                c.setPassword(rs.getString("password"));
                c.setGender(rs.getString("gender"));
                c.setPhoneNumber(rs.getString("phoneNumber"));
                c.setUsername(rs.getString("username"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return c;
    }
}
