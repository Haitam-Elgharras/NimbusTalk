package com.chat.nimbustalk.Server.dao.Impl;

import com.chat.nimbustalk.Server.dao.DBConnection;
import com.chat.nimbustalk.Server.dao.MessageDao;
import com.chat.nimbustalk.Server.dao.entities.Group;
import com.chat.nimbustalk.Server.dao.entities.Message;
import com.chat.nimbustalk.Server.dao.entities.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessageDaoImpl implements MessageDao {
    @Override
    public void save(Message o) {
        try {
            PreparedStatement pstm = DBConnection.getConnection().prepareStatement(
                    "Insert into Message (content, senderUser, receiverUser, groupe, is_groupe_message) values (?, ?, ?, ?, ?)");
            pstm.setString(1, o.getContent());
            pstm.setInt(2, o.getSender().getId());
            if (!o.is_groupe_message()) {
                pstm.setInt(3, o.getReceiver().getId());
                pstm.setObject(4, null);
            } else {
                pstm.setObject(3, null);
                pstm.setInt(4, o.getGroup().getId());
            }
            pstm.setBoolean(5, o.is_groupe_message());
            pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public Message getById(Integer id) {
        Message m = new Message();
        try {
            PreparedStatement pstm = DBConnection.getConnection()
                            .prepareStatement("Select * from message where id = ?");
            pstm.setInt(1,id);
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                m.setId(rs.getInt("id"));
                m.setContent(rs.getString("content"));
                m.setSender(new UserDaoImpl().getById(rs.getInt("senderUser")));
                m.setReceiver(new UserDaoImpl().getById(rs.getInt("receiverUser")));
                m.setCreated_at(rs.getDate("created_at"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return m;
    }

    @Override
    public List<Message> getAll() {
        ArrayList<Message> messages = new ArrayList<>();
        try {
            Statement stm = DBConnection.getConnection().createStatement();
            ResultSet rs = stm.executeQuery("Select * from Message");
            while(rs.next()){
                Message m = new Message();
                m.setId(rs.getInt("id"));
                m.setContent(rs.getString("content"));
                m.setSender(new UserDaoImpl().getById(rs.getInt("senderUser")));
                m.setReceiver(new UserDaoImpl().getById(rs.getInt("receiverUser")));
                m.setCreated_at(rs.getDate("created_at"));

                messages.add(m);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public List<Message> getAll(User sender, User receiver) {
        ArrayList<Message> messages = new ArrayList<>();
        try {
            PreparedStatement pstm = DBConnection.getConnection()
                    .prepareStatement("Select * from Message where (senderUser = ? or receiverUser = ?) and (senderUser = ? or receiverUser = ?)");
            pstm.setInt(1,sender.getId());
            pstm.setInt(2,sender.getId());
            pstm.setInt(3,receiver.getId());
            pstm.setInt(4,receiver.getId());
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                Message m = new Message();
                m.setId(rs.getInt("id"));
                m.setContent(rs.getString("content"));
                m.setSender(new UserDaoImpl().getById(rs.getInt("senderUser")));
                m.setReceiver(new UserDaoImpl().getById(rs.getInt("receiverUser")));
                m.setCreated_at(rs.getDate("created_at"));
                messages.add(m);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public List<Message> getAll(Group group) {
        ArrayList<Message> messages = new ArrayList<>();
        try {
            PreparedStatement pstm = DBConnection.getConnection()
                    .prepareStatement("Select * from Message where groupe = ? and is_groupe_message=?");
            pstm.setInt(1,group.getId());
            pstm.setBoolean(2, true);
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                Message m = new Message();
                m.setId(rs.getInt(1));
                m.setContent(rs.getString(2));
                m.setSender(new UserDaoImpl().getById(rs.getInt(3)));
                m.setReceiver(new UserDaoImpl().getById(rs.getInt(4)));
                m.setCreated_at(rs.getDate(5));
                m.setGroup(new GroupDaoImpl().getById(rs.getInt(6)));
                m.setIs_groupe_message(rs.getBoolean(7));
                messages.add(m);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return messages;
    }
}
