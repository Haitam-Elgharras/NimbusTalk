package com.chat.nimbustalk.Server.dao;

import java.util.List;

public interface DAO<T,U>{
    void save(T o);
    T getById(U id);
    List<T> getAll();
}
