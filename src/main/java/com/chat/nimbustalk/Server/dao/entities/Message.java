package com.chat.nimbustalk.Server.dao.entities;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private int id;
    private String content;
    private User sender;
    private User receiver;
    private Date created_at;
    private Group group;
    private boolean is_groupe_message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean is_groupe_message() {
        return is_groupe_message;
    }

    public void setIs_groupe_message(boolean is_groupe_message) {
        this.is_groupe_message = is_groupe_message;
    }
}
