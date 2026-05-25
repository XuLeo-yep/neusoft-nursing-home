package org.example.neuobject.nursinghouse.service;

import org.example.neuobject.nursinghouse.entity.User;

import java.util.List;

public interface UserService {

    User login(String loginCode, String password);

    void addUser(User user);

    void updateUser(User user);

    void deleteUser(long id);

    User findById(long id);

    List<User> findAll();
}
