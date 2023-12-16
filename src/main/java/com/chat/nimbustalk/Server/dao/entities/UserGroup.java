package com.chat.nimbustalk.Server.dao.entities;

import java.io.Serializable;

public class UserGroup implements Serializable {
    private User user;
    private Group group;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}

